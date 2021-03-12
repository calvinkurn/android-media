package com.tokopedia.review.feature.reviewreminder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounter
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplate
import com.tokopedia.review.feature.reviewreminder.di.component.DaggerReviewReminderComponent
import com.tokopedia.review.feature.reviewreminder.view.bottomsheet.EditMessageBottomSheet
import com.tokopedia.review.feature.reviewreminder.view.viewmodel.ReminderMessageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class ReminderMessageFragment : BaseDaggerFragment() {

    companion object {
        private const val TAG_BOTTOM_SHEET_HOW_TO = "bottomSheetHowTo"
        private const val TAG_BOTTOM_SHEET_EDIT_MESSAGE = "bottomSheetEditMessage"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: ReminderMessageViewModel? = null

    private var iconInformation: IconUnify? = null
    private var buttonEditMessage: UnifyButton? = null
    private var textSampleMessage: Typography? = null
    private var textEstimation: Typography? = null
    private var buttonSend: UnifyButton? = null
    private var rvProducts: RecyclerView? = null
    private var scrollView: ScrollView? = null
    private var cardProducts: CardUnify? = null

    private var bottomSheetHowTo: BottomSheetUnify? = null
    private var bottomSheetEditMessage: BottomSheetUnify? = null
    private var dialogSend: DialogUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderMessageViewModel::class.java)
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
        scrollView = view.findViewById(R.id.scroll_view)
        cardProducts = view.findViewById(R.id.card_products)

        initView()
        initBottomSheet()
        setupViewInteraction()
        initCoachMark()
        observeViewModel()
        fetchData()
    }

    private fun initBottomSheet() {
        bottomSheetHowTo = BottomSheetUnify()
        bottomSheetHowTo?.setTitle("Cara pakai pengingat ulasan")
        bottomSheetHowTo?.setChild(View.inflate(context, R.layout.bottom_sheet_review_reminder_how_to, null))

        bottomSheetEditMessage = EditMessageBottomSheet(textSampleMessage?.text?.toString()) { message ->
            textSampleMessage?.text = message
        }
    }

    private fun initView() {
        dialogSend = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            dialogTitle.text = getString(R.string.review_reminder_dialog_send_title)
            setPrimaryCTAText(getString(R.string.review_reminder_dialog_send_button_primary))
            setSecondaryCTAText(getString(R.string.review_reminder_dialog_send_button_secondary))
        }
    }

    private fun setupViewInteraction() {
        iconInformation?.setOnClickListener {
            bottomSheetHowTo?.show(childFragmentManager, TAG_BOTTOM_SHEET_HOW_TO)
        }

        buttonEditMessage?.setOnClickListener {
            bottomSheetEditMessage?.show(childFragmentManager, TAG_BOTTOM_SHEET_EDIT_MESSAGE)
        }

        buttonSend?.setOnClickListener { dialogSend?.show() }
    }

    private fun initCoachMark() {
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
        coachMark.showCoachMark(coachMarkItems, scrollView)
    }

    private fun observeViewModel() {
        viewModel?.getEstimation()?.observe(viewLifecycleOwner, observerEstimation)
        viewModel?.getTemplate()?.observe(viewLifecycleOwner, observerTemplate)
    }

    private fun fetchData() {
        viewModel?.fetchReminderCounter()
        viewModel?.fetchReminderTemplate()
        viewModel?.fetchProductList()
    }

    private val observerEstimation = Observer { reminderCounter: ProductrevGetReminderCounter ->
        val stringEstimation = getString(
                R.string.review_reminder_estimation,
                reminderCounter.totalBuyer,
                reminderCounter.totalProduct
        )
        textEstimation?.text = HtmlCompat.fromHtml(stringEstimation, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private val observerTemplate = Observer { template: ProductrevGetReminderTemplate ->
        textSampleMessage?.text = template.template
    }
}