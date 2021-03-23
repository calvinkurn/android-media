package com.tokopedia.review.feature.reviewreminder.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderData
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderList
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplate
import com.tokopedia.review.feature.reviewreminder.di.component.DaggerReviewReminderComponent
import com.tokopedia.review.feature.reviewreminder.view.adapter.ReviewProductAdapter
import com.tokopedia.review.feature.reviewreminder.view.bottomsheet.EditMessageBottomSheet
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderMessageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReminderMessageFragment : BaseDaggerFragment() {

    companion object {
        private const val TAG_BOTTOM_SHEET_HOW_TO = "bottomSheetHowTo"
        private const val TAG_BOTTOM_SHEET_EDIT_MESSAGE = "bottomSheetEditMessage"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ReminderMessageViewModel? = null
    private var reviewProductAdapter: ReviewProductAdapter? = null

    private val prefKey = "${this.javaClass.name}.pref"

    private var prefs: SharedPreferences? = null
    private var iconInformation: IconUnify? = null
    private var buttonEditMessage: UnifyButton? = null
    private var textSampleMessage: Typography? = null
    private var textEstimation: Typography? = null
    private var buttonSend: UnifyButton? = null
    private var rvProducts: RecyclerView? = null
    private var cardProducts: CardUnify? = null
    private var cardNoProducts: CardUnify? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var dialogSend: DialogUnify? = null

    private var estimation: ProductrevGetReminderCounter? = null
    private var productrevGetReminderList: ProductrevGetReminderList? = null
    private var products = emptyList<ProductrevGetReminderData>()
    private var isLoadProducts = false
    private var isLoadEstimation = false
    private var isLoadTemplate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderMessageViewModel::class.java)
        reviewProductAdapter = ReviewProductAdapter()
        prefs = context?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reminder_message, container, false)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val component = activity?.run {
            DaggerReviewReminderComponent.builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
        component?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iconInformation = view.findViewById(R.id.icon_information)
        buttonEditMessage = view.findViewById(R.id.button_edit_message)
        textSampleMessage = view.findViewById(R.id.text_sample_message)
        textEstimation = view.findViewById(R.id.text_estimation)
        buttonSend = view.findViewById(R.id.button_send)
        rvProducts = view.findViewById(R.id.rv_products)
        cardProducts = view.findViewById(R.id.card_products)
        cardNoProducts = view.findViewById(R.id.card_no_products)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        initView()
        setupViewInteraction()
        initCoachMark()
        observeViewModel()
        fetchData()
    }

    private fun initView() {
        dialogSend = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setPrimaryCTAText(getString(R.string.review_reminder_dialog_send_button_primary))
            setSecondaryCTAText(getString(R.string.review_reminder_dialog_send_button_secondary))
        }

        rvProducts?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewProductAdapter
            addOnScrollListener(scrollListener)
        }
    }

    private fun setupViewInteraction() {
        iconInformation?.setOnClickListener {
            val bottomSheetHowTo = BottomSheetUnify()
            bottomSheetHowTo.setTitle(getString(R.string.review_reminder_how_to_title))
            bottomSheetHowTo.setChild(View.inflate(context, R.layout.bottom_sheet_review_reminder_how_to, null))
            bottomSheetHowTo.show(childFragmentManager, TAG_BOTTOM_SHEET_HOW_TO)
        }

        buttonEditMessage?.setOnClickListener { showEditMessage() }

        buttonSend?.setOnClickListener {
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {

                setTitle(getString(R.string.review_reminder_dialog_send_title))
                val stringEstimation = getString(
                        R.string.review_reminder_dialog_description,
                        estimation?.totalBuyer ?: 0,
                        estimation?.totalProduct ?: 0
                )
                setDescription(HtmlCompat.fromHtml(stringEstimation, HtmlCompat.FROM_HTML_MODE_COMPACT))

                setPrimaryCTAText(getString(R.string.review_reminder_dialog_send_button_primary))
                setPrimaryCTAClickListener {
                    viewModel?.sendReminder(textSampleMessage?.text?.toString())
                    dismiss()
                    view?.let {
                        Toaster.build(
                                it,
                                getString(R.string.review_reminder_snackbar_sending),
                                Snackbar.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                "Oke"
                        ).show()
                    }
                    refreshData()
                }

                setSecondaryCTAText(getString(R.string.review_reminder_dialog_send_button_secondary))
                setSecondaryCTAClickListener {
                    dismiss()
                    showEditMessage()
                }

                show()
            }
        }

        swipeRefreshLayout?.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initCoachMark() {
        prefs?.let { prefs ->
            if (!prefs.getBoolean(ReviewConstants.HAS_COACHMARK_REMINDER_MESSAGE, false)) {
                val coachMarkItems = arrayListOf(
                        CoachMark2Item(
                                textSampleMessage as View,
                                getString(R.string.review_reminder_coachmark_1_title),
                                getString(R.string.review_reminder_coachmark_1_description)
                        ),
                        CoachMark2Item(
                                buttonEditMessage as View,
                                getString(R.string.review_reminder_coachmark_2_title),
                                getString(R.string.review_reminder_coachmark_2_description)
                        ),
                        CoachMark2Item(
                                cardProducts as View,
                                getString(R.string.review_reminder_coachmark_3_title),
                                getString(R.string.review_reminder_coachmark_3_description)
                        )
                )
                val coachMark = CoachMark2(requireContext())
                coachMark.setOnDismissListener { prefs.edit().putBoolean(ReviewConstants.HAS_COACHMARK_REMINDER_MESSAGE, true).apply() }
                coachMark.showCoachMark(coachMarkItems)
            }
        }
    }

    private fun observeViewModel() {
        viewModel?.getEstimation()?.observe(viewLifecycleOwner, observerEstimation)
        viewModel?.getTemplate()?.observe(viewLifecycleOwner, observerTemplate)
        viewModel?.getProducts()?.observe(viewLifecycleOwner, observerProducts)
        viewModel?.getError()?.observe(viewLifecycleOwner, observerError)
    }

    private fun fetchData() {
        isLoadEstimation = true
        isLoadTemplate = true
        isLoadProducts = true

        viewModel?.fetchReminderCounter()
        viewModel?.fetchReminderTemplate()
        viewModel?.fetchProductList()
    }

    private fun refreshData() {
        products = emptyList()
        fetchData()
    }

    private fun showEditMessage() {
        val bottomSheetEditMessage = EditMessageBottomSheet { message -> textSampleMessage?.text = message }
        bottomSheetEditMessage.message = textSampleMessage?.text?.toString()
        bottomSheetEditMessage.show(childFragmentManager, TAG_BOTTOM_SHEET_EDIT_MESSAGE)
    }

    private fun checkRefresh() {
        if (!isLoadEstimation && !isLoadTemplate && !isLoadProducts) {
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    private val observerEstimation = Observer<ProductrevGetReminderCounter> { reminderCounter ->
        val stringEstimation = getString(
                R.string.review_reminder_estimation,
                reminderCounter.totalBuyer,
                reminderCounter.totalProduct
        )
        textEstimation?.text = HtmlCompat.fromHtml(stringEstimation, HtmlCompat.FROM_HTML_MODE_COMPACT)
        estimation = reminderCounter
        isLoadEstimation = false
        checkRefresh()
    }

    private val observerTemplate = Observer<ProductrevGetReminderTemplate> { reminderTemplate ->
        val template = reminderTemplate.template
        textSampleMessage?.text = template
        isLoadTemplate = false
        checkRefresh()
    }

    private val observerProducts = Observer<Result<ProductrevGetReminderList>> { result ->
        when (result) {
            is Success -> {
                val data = result.data
                val list = data.list
                if (list.isNotEmpty()) {
                    val mergeList = products + list
                    reviewProductAdapter?.updateList(mergeList)
                    products = mergeList
                }
                if (products.isNotEmpty()) {
                    cardNoProducts?.visibility = View.GONE
                    cardProducts?.visibility = View.VISIBLE
                } else {
                    cardNoProducts?.visibility = View.VISIBLE
                    cardProducts?.visibility = View.GONE
                }
                productrevGetReminderList = data
            }
            is Fail -> {
            }
        }
        isLoadProducts = false
        checkRefresh()
    }

    private val observerError = Observer<String> {
        view?.let {
            Toaster.build(
                    it,
                    getString(R.string.review_reminder_snackbar_error),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    "Refresh",
                    View.OnClickListener {
                        refreshData()
                    }).show()
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val hasNext = productrevGetReminderList?.hasNext == true

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (!isLoadProducts && hasNext && layoutManager.findLastCompletelyVisibleItemPosition() == reviewProductAdapter?.itemCount?.minus(1)) {
                productrevGetReminderList?.let {
                    viewModel?.fetchProductList(it.lastProductID)
                    isLoadProducts = true
                }
            }
        }
    }

}
