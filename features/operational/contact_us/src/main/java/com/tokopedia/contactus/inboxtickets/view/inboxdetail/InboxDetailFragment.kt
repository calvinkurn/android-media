package com.tokopedia.contactus.inboxtickets.view.inboxdetail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.databinding.ContactUsFragmentInboxDetailBinding
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.contactus.inboxtickets.data.model.Tickets
import com.tokopedia.contactus.inboxtickets.di.DaggerInboxComponent
import com.tokopedia.contactus.inboxtickets.di.InboxModule
import com.tokopedia.contactus.inboxtickets.domain.AttachmentItem
import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.view.activity.*
import com.tokopedia.contactus.inboxtickets.view.adapter.ImageUploadAdapter
import com.tokopedia.contactus.inboxtickets.view.adapter.InboxDetailAdapter
import com.tokopedia.contactus.inboxtickets.view.customview.CustomEditText
import com.tokopedia.contactus.inboxtickets.view.fragment.CloseComplainBottomSheet
import com.tokopedia.contactus.inboxtickets.view.fragment.HelpFullBottomSheet
import com.tokopedia.contactus.inboxtickets.view.fragment.ServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxtickets.view.listeners.InboxDetailListener
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity.Companion.BUNDLE_ID_TICKET
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity.Companion.IS_OFFICIAL_STORE
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.KEY_DISLIKED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.KEY_LIKED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.RESULT_FINISH
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_CLOSED
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_IN_PROCESS
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.TICKET_STATUS_NEED_RATING
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel.Companion.FIND_KEYWORD
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel.Companion.NOT_FIND_ANY_TEXT
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel.Companion.OUT_OF_BOND
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.OnFindKeywordAtTicket
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.InboxDetailUiEffect
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel.InboxDetailUiState
import com.tokopedia.contactus.inboxtickets.view.utils.CLOSED
import com.tokopedia.contactus.inboxtickets.view.utils.Utils
import com.tokopedia.contactus.utils.CommonConstant.FIRST_INITIALIZE_ZERO
import com.tokopedia.contactus.utils.CommonConstant.INDEX_ONE
import com.tokopedia.contactus.utils.CommonConstant.INDEX_ZERO
import com.tokopedia.contactus.utils.CommonConstant.INVALID_NUMBER
import com.tokopedia.contactus.utils.CommonConstant.SIZE_ZERO
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.imagepicker.common.*
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

class TicketFragment :
    BaseDaggerFragment(),
    View.OnClickListener,
    BackTicketListener {

    companion object {
        fun newInstance(
            ticketId: String,
            isOfficialStore: Boolean
        ): TicketFragment {
            val fragment = TicketFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_ID_TICKET, ticketId)
            bundle.putBoolean(IS_OFFICIAL_STORE, isOfficialStore)
            fragment.arguments = bundle
            return fragment
        }

        private const val TEXT_MIN_LENGTH = 15
        private const val TEXT_MAX_LENGTH = 1000
        private const val DELAY_FOUR_MILLIS = 4000
        private const val INVALID_IMAGE_RESULT = -2
        const val ROLE_TYPE_AGENT = "agent"
        const val WAITING_TIME_TO_SCROLL = 300L
    }

    private val ticketId by lazy {
        arguments?.getString(BUNDLE_ID_TICKET).orEmpty()
    }

    private val isOfficialStore by lazy {
        arguments?.getBoolean(IS_OFFICIAL_STORE, false)
    }

    private var rvMessageList: RecyclerView? = null
    private var rvSelectedImages: RecyclerView? = null
    private var ivUploadImg: ImageView? = null
    private var ivSendButton: ImageView? = null
    private var edMessage: EditText? = null
    private var sendProgress: View? = null
    private var editText: CustomEditText? = null
    private var searchView: View? = null
    private var ivPrevious: ImageView? = null
    private var ivNext: ImageView? = null
    private var totalRes: TextView? = null
    private var currentRes: TextView? = null
    private var btnInactive1: ImageView? = null
    private var btnInactive2: ImageView? = null
    private var btnInactive3: ImageView? = null
    private var btnInactive4: ImageView? = null
    private var btnInactive5: ImageView? = null
    private var txtHyper: TextView? = null
    private var noTicketFound: View? = null
    private var tvNoTicket: TextView? = null
    private var tvOkButton: TextView? = null
    private var rootView: ConstraintLayout? = null
    private var tvReplyButton: TextView? = null
    private var imageUploadAdapter: ImageUploadAdapter? = null
    private var detailAdapter: InboxDetailAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var helpFullBottomSheet: HelpFullBottomSheet? = null
    private var closeComplainBottomSheet: CloseComplainBottomSheet? = null
    private var servicePrioritiesBottomSheet: ServicePrioritiesBottomSheet? = null
    private var isSendButtonEnabled = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[InboxDetailViewModel::class.java] }

    private val imagePicker= getImagePickerResultActivityLauncher()

    private val csatPageActivityForResult= getCSATResultActivityLauncher()

    @JvmField
    var mMenu: Menu? = null

    private var binding by autoClearedNullable<ContactUsFragmentInboxDetailBinding>()

    private val imageList: List<ImageUpload>
        get() = imageUploadAdapter?.getUploadedImageList() ?: emptyList()

    private val isUploadImageValid: Int
        get() {
            val uploadImageList = imageList
            if (viewModel.isNeedAttachment() && (uploadImageList.isEmpty())) {
                showMessageAndSendEvent()
                return INVALID_IMAGE_RESULT
            }
            val numOfImages = uploadImageList.size
            if (numOfImages > SIZE_ZERO) {
                val count = Utils().verifyAllImages(uploadImageList)
                return if (numOfImages == count) {
                    numOfImages
                } else {
                    INVALID_NUMBER
                }
            } else if (numOfImages == SIZE_ZERO) {
                return SIZE_ZERO
            }
            return INVALID_NUMBER
        }

    private val userMessage: String
        get() = edMessage?.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContactUsFragmentInboxDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding?.run {
            toolbar.setTitle(R.string.detail_kendala)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setObserverUIEffect()
        setObserverOnFindKeyword()
        showProgressBar()
        viewModel.ticketId(ticketId)
    }

    override fun getScreenName(): String = TicketFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerInboxComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .inboxModule(activity?.applicationContext?.let { InboxModule(it) })
            .build().inject(this)
    }

    private fun initView() {
        setupBinding()
        initImageAdapter()
        setViewListener()
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                uiStateHandle(state)
            }
        }
    }

    private fun setObserverUIEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun setObserverOnFindKeyword() {
        viewModel.onFindKeywordAtTicket.observe(viewLifecycleOwner) {
            setUiOnFindKeyword(it)
        }

        viewModel.isImageInvalid.observe(viewLifecycleOwner) {
            if (!it) {
                binding?.root?.showToasterWithCta(
                    activity?.getString(R.string.invalid_images).orEmpty()
                )
            }
        }
    }

    private fun setupBinding() {
        binding?.run {
            this@TicketFragment.rootView = rootView
            this@TicketFragment.rvMessageList = rvMessageList
            this@TicketFragment.rvSelectedImages = layoutReplayMessage.rvSelectedImages
            this@TicketFragment.ivUploadImg = layoutReplayMessage.ivUploadImg
            this@TicketFragment.ivSendButton = layoutReplayMessage.ivSendButton
            this@TicketFragment.edMessage = layoutReplayMessage.edMessage
            this@TicketFragment.sendProgress = layoutReplayMessage.sendProgress
            this@TicketFragment.editText = customSearch
            this@TicketFragment.searchView = inboxSearchView
            this@TicketFragment.ivPrevious = ivPreviousUp
            this@TicketFragment.ivNext = ivNextDown
            this@TicketFragment.totalRes = tvCountTotal
            this@TicketFragment.currentRes = tvCountCurrent
            this@TicketFragment.btnInactive1 = viewHelpRate.btnInactive1
            this@TicketFragment.btnInactive2 = viewHelpRate.btnInactive2
            this@TicketFragment.btnInactive3 = viewHelpRate.btnInactive3
            this@TicketFragment.btnInactive4 = viewHelpRate.btnInactive4
            this@TicketFragment.btnInactive5 = viewHelpRate.btnInactive5
            this@TicketFragment.txtHyper = viewLinkBottom.txtHyper
            this@TicketFragment.tvReplyButton = viewRplyBottonBeforeCsatRating.tvReplyButton
            this@TicketFragment.noTicketFound = noTicketFound.root
            this@TicketFragment.tvNoTicket = noTicketFound.tvNoTicket
            this@TicketFragment.tvOkButton = noTicketFound.tvOkButton
            layoutManager = LinearLayoutManager(
                context ?: return,
                LinearLayoutManager.VERTICAL,
                false
            )
            this@TicketFragment.rvMessageList?.layoutManager =
                layoutManager
        }
    }

    private fun initImageAdapter() {
        imageUploadAdapter = ImageUploadAdapter(
            context ?: return,
            object :
                ImageUploadAdapter.OnSelectImageClick {
                override fun onClick() {
                    showImagePickerDialog()
                }
            }
        ) {
            rvSelectedImages?.hide()
            rvMessageList?.setPadding(
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                activity?.resources?.getDimensionPixelSize(
                    R.dimen.contact_us_text_toolbar_height_collapsed
                )
                    ?: return@ImageUploadAdapter
            )
            Unit
        }

        rvSelectedImages?.layoutManager =
            LinearLayoutManager(context ?: return, LinearLayoutManager.HORIZONTAL, false)
        rvSelectedImages?.adapter = imageUploadAdapter
    }

    private fun setViewListener() {
        edMessage?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length >= TEXT_MIN_LENGTH) {
                    setSubmitButtonEnabled(true)
                } else {
                    setSubmitButtonEnabled(false)
                }
                if (s.length == TEXT_MAX_LENGTH) {
                    binding?.root.showErrorToasterWithCta(
                        getString(R.string.contact_us_maximum_length_error_text)
                    )
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        editText?.setListener(object : CustomEditText.Listener {
            override fun onSearchSubmitted(text: String) {
                viewModel.onSearchSubmitted(text)
            }

            override fun onSearchTextChanged(text: String) {
                viewModel.onSearchSubmitted(text)
            }
        })

        btnInactive1?.setOnClickListener(this)
        btnInactive2?.setOnClickListener(this)
        btnInactive3?.setOnClickListener(this)
        btnInactive4?.setOnClickListener(this)
        btnInactive5?.setOnClickListener(this)
        ivUploadImg?.setOnClickListener(this)
        ivSendButton?.setOnClickListener(this)
        ivNext?.setOnClickListener(this)
        ivPrevious?.setOnClickListener(this)
        txtHyper?.setOnClickListener(this)
        tvReplyButton?.setOnClickListener(this)
    }

    private fun showImagePickerDialog() {
        val builder = ImagePickerBuilder.getOriginalImageBuilder(context ?: return)
        val intent =
            RouteManager.getIntent(context ?: return, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        intent.putParamPageSource(ImagePickerPageSource.INBOX_DETAIL_PAGE)
        goToImagePicker(intent)
    }

    private fun goToImagePicker(intent : Intent){
        imagePicker.launch(intent)
    }

    private fun onImageSelect(image: ImageUpload) {
        if (image.fileLoc?.let { Utils().fileSizeValid(it) } == false) {
            binding?.root?.showErrorToasterWithCta(
                activity?.getString(R.string.error_msg_wrong_size).orEmpty()
            )
            sendTrackerImageError1()
        } else if (image.fileLoc?.let { Utils().isBitmapDimenValid(it) } == false) {
            binding?.root?.showErrorToasterWithCta(
                activity?.getString(R.string.error_msg_wrong_height_width).orEmpty()
            )
            sendTrackerImageError2()
        } else {
            addImage(image)
        }
    }

    private fun sendTrackerImageError1() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickAttachImage,
            InboxTicketTracking.Label.ImageError1
        )
    }

    private fun sendTrackerImageError2() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickAttachImage,
            InboxTicketTracking.Label.ImageError2
        )
    }

    private fun addImage(image: ImageUpload) {
        if (rvSelectedImages?.visibility != View.VISIBLE) {
            rvSelectedImages?.show()
            rvMessageList?.setPadding(
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                activity?.resources?.getDimensionPixelSize(
                    R.dimen.contact_us_text_toolbar_height_expanded
                )
                    ?: return
            )
        }
        imageUploadAdapter?.addImage(image)
    }

    private fun handleEffect(uiEffect: InboxDetailUiEffect) {
        when (uiEffect) {
            is InboxDetailUiEffect.SendCSATRatingSuccess -> {
                hideProgressBar()
                binding?.root?.showToasterWithCta(
                    activity?.getString(R.string.cu_terima_kasih_atas_masukannya).orEmpty()
                )
                showIssueClosed()
                updateClosedStatus()
                sendGTMEventClickSubmitCsatRating(
                    uiEffect.ticketNumber,
                    uiEffect.rating,
                    uiEffect.reason
                )
            }

            is InboxDetailUiEffect.SendCSATRatingFailed -> {
                hideProgressBar()
                if (uiEffect.throwable != null) {
                    binding?.root?.showErrorToasterWithCta(
                        activity?.getString(R.string.contact_us_failed).orEmpty()
                    )
                } else {
                    binding?.root?.showErrorToasterWithCta(uiEffect.messageError)
                }
            }

            is InboxDetailUiEffect.GetDetailInboxDetailFailed -> {
                hideProgressBar()
                if (uiEffect.throwable != null) {
                    binding?.root?.showErrorToasterWithCta(
                        activity?.getString(R.string.contact_us_something_went_wrong).orEmpty()
                    )
                } else {
                    binding?.root?.showErrorToasterWithCta(uiEffect.messageError)
                }
            }

            is InboxDetailUiEffect.OnSearchInboxDetailKeyword -> {
                hideProgressBar()
                enterSearchMode(uiEffect.searchKeyword, uiEffect.sizeSearch)
                if (uiEffect.sizeSearch > SIZE_ZERO) {
                    sendTrackerSearchFindResult()
                } else {
                    binding?.root?.showErrorToasterWithCta(
                        activity?.getString(R.string.no_search_result).orEmpty()
                    )
                    sendTrackerSearchNotFindResult()
                }
            }

            is InboxDetailUiEffect.OnSearchInboxDetailKeywordFailed -> {
                hideProgressBar()
                binding?.root?.showToasterWithCta(
                    activity?.getString(R.string.contact_us_something_went_wrong).orEmpty()
                )
            }

            is InboxDetailUiEffect.OnCloseInboxDetailSuccess -> {
                Observable.timer(DELAY_FOUR_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Long>() {
                        override fun onCompleted() {}
                        override fun onError(e: Throwable) {}
                        override fun onNext(aLong: Long) {
                            showProgressBar()
                            viewModel.refreshLayout()
                        }
                    })
                onClickEmoji(INDEX_ZERO, uiEffect.ticketNumber)
            }

            is InboxDetailUiEffect.OnCloseInboxDetailFailed -> {
                if (uiEffect.throwable != null) {
                    binding?.root?.showErrorToasterWithCta(
                        activity?.getString(R.string.contact_us_failed).orEmpty()
                    )
                } else {
                    binding?.root?.showErrorToasterWithCta(uiEffect.messageError)
                }
            }

            is InboxDetailUiEffect.SendTextMessageSuccess -> {
                hideSendProgress()
                updateAddComment(uiEffect.commentItems)
            }

            is InboxDetailUiEffect.SendTextMessageFailed -> {
                hideSendProgress()
                when {
                    (uiEffect.messageError.isEmpty() && uiEffect.throwable == null) || uiEffect.throwable != null -> {
                        binding?.rootView?.showErrorToasterWithCta(
                            activity?.getString(R.string.contact_us_sent_error_message).orEmpty()
                        )
                    }

                    uiEffect.messageError.isNotEmpty() -> {
                        binding?.rootView?.showErrorToasterWithCta(uiEffect.messageError)
                    }
                }
            }

            is InboxDetailUiEffect.OnSendRatingSuccess -> {
                hideProgressBar()
                onSuccessSubmitOfRating(
                    uiEffect.rating,
                    uiEffect.commentItem,
                    uiEffect.commentPosition
                )
            }

            is InboxDetailUiEffect.OnSendRatingFailed -> {
                hideProgressBar()
                if (uiEffect.throwable != null) {
                    binding?.root?.showErrorToasterWithCta(
                        activity?.getString(R.string.contact_us_failed).orEmpty()
                    )
                } else {
                    binding?.root?.showErrorToasterWithCta(uiEffect.messageError)
                }
            }
        }
    }

    private fun handlingSubmitCsatRating(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            showProgressBar()
            val dataResult = result.data
            val rating = dataResult?.getLongExtra(BaseFragmentProvideRating.EMOJI_STATE, 0L).orZero()
            val reason = dataResult?.getStringExtra(BaseFragmentProvideRating.SELECTED_ITEM).orEmpty()
            viewModel.submitCsatRating(reason, rating.toInt())
        }
    }

    private fun sendGTMEventClickSubmitCsatRating(number: String, rating: Int, reason: String) {
        val captions = activity?.resources?.getStringArray(R.array.contactus_csat_caption)
        val caption =
            if (rating == FIRST_INITIALIZE_ZERO) "" else captions?.get(rating - INDEX_ONE).orEmpty()
        val reasonListAsInt = reason.split(";")
        val reasonListAsString = viewModel.getReasonListAsString(reasonListAsInt)
        val reasonAsString = reasonListAsString.joinToString(";")
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickSubmitCsatRating,
            "$number - $caption - $reasonAsString"
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_search) {
            toggleSearch(View.VISIBLE, item)
            enterSearchMode("", INVALID_NUMBER)
            true
        } else {
            false
        }
    }

    private fun toggleSearch(visibility: Int, menu: MenuItem) {
        searchView?.visibility = visibility
        menu.isVisible = visibility != View.VISIBLE
    }

    private fun toggleTextToolbar(visibility: Int) {
        if (visibility == View.VISIBLE) {
            binding?.viewHelpRate?.root?.hide()
            rvMessageList?.setPadding(
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                activity?.resources?.getDimensionPixelSize(
                    R.dimen.contact_us_text_toolbar_height_collapsed
                )
                    ?: return
            )
        } else {
            binding?.viewHelpRate?.root?.show()
            rvMessageList?.setPadding(
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                FIRST_INITIALIZE_ZERO,
                activity?.resources?.getDimensionPixelSize(R.dimen.help_rate_height) ?: return
            )
        }
        binding?.layoutReplayMessage?.root?.visibility = visibility
    }

    private fun showIssueClosed() {
        binding?.viewHelpRate?.root?.hide()
        binding?.layoutReplayMessage?.root?.hide()
        binding?.viewLinkBottom?.root?.show()
        rvMessageList?.setPadding(
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            activity?.resources?.getDimensionPixelSize(
                R.dimen.contact_us_text_toolbar_height_collapsed
            )
                ?: return
        )
    }

    private fun updateClosedStatus() {
        if (!viewModel.isCommentEmpty()) {
            viewModel.setLastCommentAsClosed()
            detailAdapter?.notifyItemChanged(INDEX_ZERO)
        }
    }

    private fun clearSearch() {
        editText?.setText("")
    }

    private fun exitSearchMode() {
        detailAdapter?.exitSearchMode()
    }

    private fun showProgressBar() {
        binding?.progressBarLayout?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding?.progressBarLayout?.visibility = View.GONE
    }

    private fun enterSearchMode(search: String, total: Int) {
        binding?.layoutReplayMessage?.root?.hide()
        binding?.viewHelpRate?.root?.hide()
        binding?.viewLinkBottom?.root?.hide()
        detailAdapter?.enterSearchMode(search)
        val placeHolder = "/%s"
        if (total <= SIZE_ZERO) {
            if (total == SIZE_ZERO) {
                currentRes?.text = "0"
                totalRes?.text = "/0"
            } else {
                currentRes?.text = ""
                totalRes?.text = ""
            }
            ivPrevious?.isClickable = false
            ivNext?.isClickable = false
        } else {
            totalRes?.text = String.format(placeHolder, total.toString())
            ivPrevious?.isClickable = true
            ivNext?.isClickable = true
            onClickNextPrev(ivPrevious)
        }
        rvMessageList?.setPadding(
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO
        )
    }

    private fun onClickNextPrev(v: View?) {
        val id = v?.id
        if (id == R.id.iv_next_down) {
            viewModel.getNextResult()
        } else {
            viewModel.getPreviousResult()
        }
    }

    private fun scrollToResult(index: Int) {
        if (index != INVALID_NUMBER) {
            layoutManager?.scrollToPositionWithOffset(index, INDEX_ZERO)
        }
    }

    private fun setUiOnFindKeyword(onSearch: OnFindKeywordAtTicket) {
        when (onSearch.status) {
            NOT_FIND_ANY_TEXT -> {
                binding?.root?.showToasterWithCta(
                    activity?.resources?.getString(R.string.no_search_result).orEmpty()
                )
            }
            OUT_OF_BOND -> {
                binding?.root?.showToasterWithCta(
                    activity?.resources?.getString(R.string.cu_no_more_results).orEmpty()
                )
            }
            FIND_KEYWORD -> {
                val adapterPosition = onSearch.positionAdapter
                currentRes?.text = onSearch.positionKeyword.toString()
                scrollToResult(adapterPosition)
            }
        }
    }

    private fun uiStateHandle(state: InboxDetailUiState) {
        renderMessageList(state.ticketDetail)
    }

    private fun View?.showToasterWithCta(message: String) {
        val ctaText = activity?.getString(R.string.contact_us_ok).orEmpty()
        Toaster.build(
            this ?: return,
            message,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_NORMAL,
            ctaText
        ).apply {
            show()
        }
    }

    private fun View?.showErrorToasterWithCta(message: String) {
        val ctaText = activity?.getString(R.string.contact_us_ok).orEmpty()
        Toaster.build(
            this ?: return,
            message,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            ctaText
        ).apply {
            show()
        }
    }

    fun setSubmitButtonEnabled(enabled: Boolean) {
        isSendButtonEnabled = enabled
        if (enabled) {
            ivSendButton?.setColorFilter(
                ContextCompat.getColor(
                    context ?: return,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
        } else {
            ivSendButton?.clearColorFilter()
        }
    }

    private fun onClickEmoji(emojiNumber: Int, ticketNumber: String) {
        sendGTMEventView()
        sendGTMEventClick(emojiNumber, ticketNumber)
        val intentToPageCsat = ContactUsProvideRatingActivity.getInstance(
            activity as Context,
            emojiNumber,
            viewModel.getFirstCommentId(),
            viewModel.getCSATBadReasonList() as ArrayList<BadCsatReasonListItem>
        )
        goToCSATPage(intentToPageCsat)
    }

    private fun goToCSATPage(intentToPageCsat : Intent){
        csatPageActivityForResult.launch(intentToPageCsat)
    }

    private fun sendGTMEventView() {
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.EventView,
            InboxTicketTracking.Category.EventHelpMessageInbox,
            InboxTicketTracking.Action.EventImpressionOnCsatRating,
            ticketId
        )
    }

    private fun sendGTMEventClick(number: Int, ticketNumber: String) {
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickOnCsatRating,
            "$ticketNumber - $number"
        )
    }

    fun sendComplain(agreed: Boolean) {
        closeComplainBottomSheet?.dismiss()
        if (agreed) {
            sendGTmEvent(
                InboxTicketTracking.Label.EventLabelYaTutup,
                InboxTicketTracking.Action.EventClickCloseTicket
            )
            viewModel.closeTicket()
        } else {
            sendGTmEvent(
                InboxTicketTracking.Label.EventLabelBatal,
                InboxTicketTracking.Action.EventClickCloseTicket
            )
            binding?.viewRplyBottonBeforeCsatRating?.root?.hide()
            binding?.viewHelpRate?.root?.hide()
            binding?.layoutReplayMessage?.root?.show()
        }
    }

    private fun sendGTmEvent(eventLabel: String, action: String) {
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            action,
            "$ticketId - $eventLabel"
        )
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.iv_upload_img) {
            onClickUpload()
        } else if (id == R.id.btn_inactive_1 || id == R.id.btn_inactive_2 || id == R.id.btn_inactive_3 || id == R.id.btn_inactive_4 || id == R.id.btn_inactive_5) {
            onEmojiClick(view)
        } else if (id == R.id.iv_send_button) {
            sendMessage(isSendButtonEnabled)
        } else if (id == R.id.txt_hyper) {
            onClickListener(view)
        } else if (id == R.id.iv_next_down || id == R.id.iv_previous_up) {
            onClickNextPrev(view)
        } else {
            if (view.id == R.id.tv_reply_button) {
                val rating = viewModel.getTicketRating().toIntOrZero()
                if (rating == KEY_LIKED || rating == KEY_DISLIKED) {
                    binding?.viewRplyBottonBeforeCsatRating?.root?.hide()
                    binding?.layoutReplayMessage?.root?.show()
                } else {
                    helpFullBottomSheet = HelpFullBottomSheet()
                    helpFullBottomSheet?.setCloseListener(object :
                        HelpFullBottomSheet.CloseSHelpFullBottomSheet {
                        override fun onClick(agreed: Boolean) {
                            setHelpOnClick(agreed)
                        }
                    })
                    helpFullBottomSheet?.show(parentFragmentManager, "helpFullBottomSheet")

                    binding?.viewRplyBottonBeforeCsatRating?.root?.hide()
                    binding?.layoutReplayMessage?.root?.show()
                }
            }
        }
    }

    fun setHelpOnClick(agreed: Boolean) {
        var item: CommentsItem? = null
        var commentPosition = FIRST_INITIALIZE_ZERO
        for (i in (detailAdapter?.itemCount?.minus(INDEX_ONE).orZero()) downTo INDEX_ZERO) {
            val item1 = viewModel.getCommentOnPosition(i)
            if (item1.createdBy.role == "agent") {
                item = item1
                commentPosition = i
                break
            }
        }
        if (agreed) {
            binding?.viewRplyBottonBeforeCsatRating?.root?.hide()
            viewModel.sendRating(true, commentPosition, item?.id ?: "")
            helpFullBottomSheet?.dismiss()

            sendGTmEvent(
                InboxTicketTracking.Label.EventLabelYa,
                InboxTicketTracking.Action.EventRatingCsatOnSlider
            )

            if (viewModel.isTicketAllowClose()) {
                closeComplainBottomSheet = CloseComplainBottomSheet()
                closeComplainBottomSheet?.setCloseListener(object :
                    CloseComplainBottomSheet.CloseComplainBottomSheetListner {
                    override fun onClickComplain(agreed: Boolean) {
                        sendComplain(agreed)
                    }
                })
                closeComplainBottomSheet?.show(parentFragmentManager, "closeComplainBottomSheet")
            } else {
                binding?.layoutReplayMessage?.root?.show()
            }
        } else {
            sendGTmEvent(
                InboxTicketTracking.Label.EventLabelTidak,
                InboxTicketTracking.Action.EventRatingCsatOnSlider
            )
            viewModel.sendRating(false, commentPosition, item?.id ?: "")
            binding?.layoutReplayMessage?.root?.show()
            helpFullBottomSheet?.dismiss()
        }
    }

    private fun onClickUpload() {
        showImagePickerDialog()
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickAttachImage,
            ""
        )
    }

    private fun onEmojiClick(v: View) {
        when (v.id) {
            R.id.btn_inactive_1 -> onClickEmoji(1, viewModel.getTicketNumber())
            R.id.btn_inactive_2 -> onClickEmoji(2, viewModel.getTicketNumber())
            R.id.btn_inactive_3 -> onClickEmoji(3, viewModel.getTicketNumber())
            R.id.btn_inactive_4 -> onClickEmoji(4, viewModel.getTicketNumber())
            R.id.btn_inactive_5 -> onClickEmoji(5, viewModel.getTicketNumber())
        }
    }

    private fun sendMessage(isSendButtonEnabled: Boolean) {
        if (isSendButtonEnabled) {
            sendMessage()
        } else {
            binding?.root?.showErrorToasterWithCta(
                activity?.getString(R.string.contact_us_minimum_length_error_text) ?: return
            )
        }
    }

    private fun sendMessage() {
        showSendProgress()
        viewModel.sendMessage(isUploadImageValid, imageList, message = userMessage)
        edMessage?.setHint(R.string.contact_us_type_here)
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickReplyTicket,
            ticketId
        )
    }

    private fun showSendProgress() {
        sendProgress?.show()
        ivSendButton?.invisible()
    }

    private fun hideSendProgress() {
        sendProgress?.hide()
        ivSendButton?.show()
    }

    private fun showMessageAndSendEvent() {
        binding?.rootView?.showToasterWithCta(
            activity?.getString(R.string.attachment_required).orEmpty()
        )
        hideSendProgress()
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventNotAttachImageRequired,
            ""
        )
    }

    private fun updateAddComment(newItem: CommentsItem) {
        edMessage?.text?.clear()
        setSubmitButtonEnabled(false)
        imageUploadAdapter?.clearAll()
        imageUploadAdapter?.notifyItemChanged(imageUploadAdapter!!.itemCount - INDEX_ONE)
        rvSelectedImages?.hide()
        rvMessageList?.setPadding(
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            FIRST_INITIALIZE_ZERO,
            activity?.resources?.getDimensionPixelSize(
                R.dimen.contact_us_text_toolbar_height_collapsed
            )
                ?: return
        )
        detailAdapter?.setNeedAttachment(false)
        detailAdapter?.addComment(newItem)
    }

    private fun sendTrackerSearchFindResult() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickSearchDetails,
            InboxTicketTracking.Label.GetResult
        )
    }

    private fun sendTrackerSearchNotFindResult() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickSearchDetails,
            InboxTicketTracking.Label.NoResult
        )
    }

    private fun onClickListener(v: View) {
        val id = v.id
        if (id == R.id.txt_hyper) {
            activity?.setResult(RESULT_FINISH)
            ContactUsTracking.sendGTMInboxTicket(
                "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickHubungi,
                InboxTicketTracking.Label.TicketClosed
            )
            activity?.finish()
        }
    }

    private fun onSuccessSubmitOfRating(rating: Int, comment: CommentsItem, commentPosition: Int) {
        val item = comment
        val rate =
            if (rating == KEY_LIKED) KEY_LIKED else KEY_DISLIKED
        item.rating = rate.toString()
        detailAdapter?.notifyItemChanged(commentPosition, item)
    }

    private fun renderMessageList(ticketDetail: Tickets) {
        edMessage?.text?.clear()
        setSubmitButtonEnabled(false)
        binding?.viewHelpRate?.root?.hide()
        binding?.layoutReplayMessage?.root?.show()
        val ticketStatus = viewModel.getTicketStatus()
        if (ticketDetail.getTicketComment().isNotEmpty()) {
            updateUiBasedOnStatus(ticketDetail)
            setHeaderPriorityLabel(ticketDetail)
            detailAdapter = InboxDetailAdapter(
                context ?: return,
                ticketDetail.getTicketComment() as ArrayList,
                ticketDetail.isNeedAttachment,
                object : InboxDetailListener {
                    override fun onCommentClick(
                        agreed: Boolean,
                        commentPosition: Int,
                        commentId: String
                    ) {
                        viewModel.sendRating(agreed, commentPosition, commentId)
                    }

                    override fun onPriorityLabelClick() {
                        onCommentPriorityLabelClick()
                    }

                    override fun onTransactionDetailsClick() {
                        onCommentTransactionDetailsClick()
                    }

                    override fun showImageAttachment(
                        position: Int,
                        imagesURL: List<AttachmentItem>
                    ) {
                        showImagePreview(position, imagesURL)
                    }

                    override fun scrollTo(position: Int) {
                        scrollToPosition(position)
                    }
                },
                viewModel.getUserId(),
                ticketId,
                ticketStatus
            )
            rvMessageList?.adapter = detailAdapter
            rvMessageList?.show()
            scrollToPosition(detailAdapter?.itemCount?.minus(INDEX_ONE).orZero())
        } else {
            rvMessageList?.hide()
        }
        hideProgressBar()
    }

    private fun scrollToPosition(position: Int) {
        Observable.timer(WAITING_TIME_TO_SCROLL, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Long>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {}
                override fun onNext(aLong: Long) {
                    if ((rvMessageList?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() != position) scrollToResult(
                        position
                    )
                }
            })
    }

    private fun updateUiBasedOnStatus(ticketDetail: Tickets) {
        val lastCommentStatus = ticketDetail.getTicketComment()[INDEX_ZERO].ticketStatus
        val lastComment =
            ticketDetail.getTicketComment()[ticketDetail.getTicketComment().size - INDEX_ONE]
        when (lastCommentStatus) {
            TICKET_STATUS_IN_PROCESS -> {
                rvMessageList?.setPadding(
                    FIRST_INITIALIZE_ZERO,
                    FIRST_INITIALIZE_ZERO,
                    FIRST_INITIALIZE_ZERO,
                    activity?.resources?.getDimensionPixelSize(
                        R.dimen.contact_us_text_toolbar_height_collapsed
                    )
                        ?: return
                )
                if (ROLE_TYPE_AGENT.equals(
                        lastComment.createdBy.role,
                        ignoreCase = true
                    ) && "".equals(lastComment.rating, ignoreCase = true)
                ) {
                    binding?.viewRplyBottonBeforeCsatRating?.root?.show()
                }
                if (ticketDetail.isShowRating) {
                    toggleTextToolbar(View.GONE)
                }
            }
            TICKET_STATUS_CLOSED -> {
                showIssueClosed()
            }
            TICKET_STATUS_NEED_RATING -> {
                toggleTextToolbar(View.GONE)
            }
        }
    }

    private fun setHeaderPriorityLabel(ticketDetail: Tickets) {
        ticketDetail.getTicketComment()[INDEX_ZERO].priorityLabel = isOfficialStore.orFalse()
        viewModel.setLastCommentAsOfficialStore(isOfficialStore.orFalse())
    }

    fun onCommentPriorityLabelClick() {
        servicePrioritiesBottomSheet = ServicePrioritiesBottomSheet()
        servicePrioritiesBottomSheet?.setCloseButtonListener(
            object :
                ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet {
                override fun onClickClose() {
                    servicePrioritiesBottomSheet?.dismiss()
                }
            }
        )
        servicePrioritiesBottomSheet?.show(parentFragmentManager, "servicePrioritiesBottomSheet")
    }

    fun onCommentTransactionDetailsClick() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickDetailTrasanksi,
            ""
        )
    }

    fun showImagePreview(position: Int, imagesAttachment: List<AttachmentItem>) {
        sendTrackingImagePreview()
        val imagesUrl = ArrayList<String>()
        for (item in imagesAttachment) {
            if (item.url?.isNotEmpty() == true) {
                imagesUrl.add(item.url ?: "")
            } else imagesUrl.add(item.thumbnail ?: "")
        }
        startActivity(
            ImagePreviewActivity.getCallingIntent(
                context ?: return,
                imagesUrl,
                null,
                position
            )
        )
    }

    private fun sendTrackingImagePreview() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickAttachImage,
            InboxTicketTracking.Label.ImageAttached
        )
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.contactus_menu_details, menu)
        mMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuBackPress(): Boolean {
        if (searchView?.visibility == View.VISIBLE) {
            mMenu?.findItem(R.id.action_search)?.let { toggleSearch(View.GONE, it) }
            if (viewModel.isShowRating()) {
                toggleTextToolbar(View.GONE)
            } else if (viewModel.getTicketStatus()
                    .equals(CLOSED, ignoreCase = true) && !viewModel.isShowRating()
            ) {
                showIssueClosed()
            } else {
                toggleTextToolbar(View.VISIBLE)
            }
            clearSearch()
            exitSearchMode()
            return true
        } else {
            return false
        }
    }

    override fun onDeviceBackPress() {
        onBackPressed()
    }

    private fun onBackPressed() {
        if ((
                imageUploadAdapter?.itemCount.orZero()
                ) > 1 || binding?.layoutReplayMessage?.root?.visibility == View.VISIBLE &&
            edMessage?.isFocused == true && edMessage?.text?.isNotEmpty() == true && parentFragmentManager.backStackEntryCount <= 0
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.inbox_title_dialog_wrong_scan))
            builder.setMessage(R.string.abandon_message_warning)
            builder.setNegativeButton(
                getString(R.string.inbox_cancel)
            ) { dialog: DialogInterface, i: Int -> dialog.dismiss() }
            builder.setPositiveButton(
                getString(R.string.inbox_exit)
            ) { _, _ ->
                sendTrackerBackPress()
                activity?.finish()
            }.create().show()
        } else {
            activity?.finish()
        }
    }

    private fun sendTrackerBackPress() {
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventAbandonReplySubmission,
            getString(R.string.inbox_cancel)
        )
    }

    private fun getImagePickerResultActivityLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handleImageResult(it)
        }
    }

    private fun handleImageResult(it: ActivityResult){
        if (it.resultCode == Activity.RESULT_OK) {
            val imagePathList = ImagePickerResultExtractor.extract(it.data).imageUrlOrPathList
            if (imagePathList.size > SIZE_ZERO) {
                val imagePath = imagePathList[INDEX_ZERO]
                if (!TextUtils.isEmpty(imagePath)) {
                    val position = imageUploadAdapter?.itemCount.orZero()
                    val image = ImageUpload()
                    image.position = position
                    image.imageId = "image" + UUID.randomUUID().toString()
                    image.fileLoc = imagePath
                    onImageSelect(image)
                }
            }
        }
    }

    private fun getCSATResultActivityLauncher(): ActivityResultLauncher<Intent> {
         return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                 handlingSubmitCsatRating(it)
         }
     }
}

interface BackTicketListener {
    fun onMenuBackPress(): Boolean
    fun onDeviceBackPress()
}
