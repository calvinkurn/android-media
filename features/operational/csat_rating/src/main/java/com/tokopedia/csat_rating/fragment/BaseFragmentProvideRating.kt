package com.tokopedia.csat_rating.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.csat_rating.ProvideRatingContract
import com.tokopedia.csat_rating.R
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.di.component.DaggerCsatComponent
import com.tokopedia.csat_rating.di.module.CsatRatingModule
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIFTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIRST_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FOURTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.SECOND_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.THIRD_EMOJI
import com.tokopedia.csat_rating.presenter.screenState.ScreenState
import com.tokopedia.csat_rating.presenter.screenState.ZeroScreenState
import com.tokopedia.csat_rating.quickfilter.QuickFilterItem
import com.tokopedia.csat_rating.quickfilter.QuickSingleFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject


open class BaseFragmentProvideRating : BaseDaggerFragment(),
    ProvideRatingContract.ProvideRatingView {

    protected lateinit var mTxtHelpTitle: TextView
    protected lateinit var mSmileLayout: LinearLayout
    protected lateinit var mTxtSmileSelected: TextView
    protected lateinit var mTxtFeedbackQuestion: TextView
    protected lateinit var mTxtFinished: TextView
    private var progress: ProgressDialog? = null
    private var selectedOption: MutableList<String> = ArrayList()
    protected lateinit var mFilterReview: QuickSingleFilterView
    private var reviewLength: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: BaseProvideRatingFragmentViewModel? = null

    companion object {
        const val CSAT_TITLE = "csatTitle"
        const val CLICKED_EMOJI = "clicked_emoji"
        const val PARAM_OPTIONS_CSAT = "options_csat"
        const val UPDATING = "Updating"
        const val CAPTION_LIST = "captionList"
        const val QUESTION_LIST = "questionList"
        const val NO_EMOJI = 0
        const val minLength = 1
        const val maxLength = 29
        const val EMOJI_STATE = "emoji_state"
        const val SELECTED_ITEM = "selected_items"

        fun newInstance(bundle: Bundle): BaseFragmentProvideRating {
            val fragment = BaseFragmentProvideRating()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun initInjector() {
        DaggerCsatComponent.builder().baseAppComponent(
            ((activity as Activity).application as BaseMainApplication).baseAppComponent
        )
            .csatRatingModule(context?.let { CsatRatingModule(it) })
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.csat_fragment_rating_provide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initObserver()
        initView(view)

        val caption = arguments?.getStringArrayList(CAPTION_LIST).orEmpty()
        viewModel?.setCaption(caption as ArrayList<String>)
        val question = arguments?.getStringArrayList(QUESTION_LIST).orEmpty()
        viewModel?.setQuestion(question as ArrayList<String>)
        val reasonItemList : ArrayList<BadCsatReasonListItem> = arguments?.getParcelableArrayList(PARAM_OPTIONS_CSAT) ?: ArrayList()
        viewModel?.setReasonList(reasonItemList)
        val emojiState =  arguments?.getInt(CLICKED_EMOJI) ?: NO_EMOJI
        viewModel?.setSelectedEmoji(emojiState)

        mTxtHelpTitle.text = arguments?.getString(CSAT_TITLE) ?: ""
        disableSubmitButton()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BaseProvideRatingFragmentViewModel::class.java)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun setFirstEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener { viewModel?.setSelectedEmoji(FIRST_EMOJI) }
    }

    override fun setSecondEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener { viewModel?.setSelectedEmoji(SECOND_EMOJI) }
    }

    override fun setThirdEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener { viewModel?.setSelectedEmoji(THIRD_EMOJI) }
    }

    override fun setFourthEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener { viewModel?.setSelectedEmoji(FOURTH_EMOJI) }
    }

    override fun setFifthEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener { viewModel?.setSelectedEmoji(FIFTH_EMOJI) }
    }

    override fun setMessage(message: String) {
        mTxtSmileSelected.text = message

    }

    override fun setMessageColor(color: Int) {
        mTxtSmileSelected.setTextColor(MethodChecker.getColor(context, color))
    }

    override fun setQuestion(question: String) {
        mTxtFeedbackQuestion.text = question
    }

    private fun addImageView(drawable: Int): ImageView {
        val imageView = ImageView(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.weight = 1.0f
        imageView.setImageResource(drawable)
        imageView.layoutParams = layoutParams
        mSmileLayout.addView(imageView)
        return imageView
    }

    override fun getSelectedEmoji(): Int {
        return arguments?.getInt(CLICKED_EMOJI) ?: NO_EMOJI

    }

    override fun clearEmoji() {
        mSmileLayout.removeAllViews()
    }

    override fun showErrorMessage(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }

    }


    open fun getLayoutManager(filterList: List<BadCsatReasonListItem>): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun setFilterList(filterList: List<BadCsatReasonListItem>) {
        mFilterReview.updateLayoutManager(getLayoutManager(filterList))
        val filterItems = ArrayList<QuickFilterItem>()
        var finishFilter: QuickFilterItem? = null
        for (filter in filterList) {
            finishFilter = QuickFilterItem()
            finishFilter.name = filter.message
            finishFilter.type = filter.id.toString()
            finishFilter.setColorBorder(com.tokopedia.unifyprinciples.R.color.Unify_GN400)
            filterItems.add(finishFilter)
        }
        mFilterReview.renderFilter(filterItems)
        mFilterReview.setListener(object : QuickSingleFilterView.ActionListener {
            override fun selectFilter(typeFilter: String?) {
                if (selectedOption.contains(typeFilter)) {
                    selectedOption.remove(typeFilter)
                } else {
                    if (!(filterList.isNotEmpty() && filterList[0].id > 0)) {
                        sendEventClickReason(filterList[typeFilter?.toIntOrNull() ?: 0].message)
                    }
                    if (typeFilter != null) {
                        selectedOption.add(typeFilter)
                    }
                }
                handleSubmitButtonState()
            }
        })
    }

    open fun sendEventClickReason(message: String?) {}

    override fun getSelectedItem(): String {
        var filters = ""
        for (filter in selectedOption) {
            filters += "$filter;"
        }
        if (filters.isNotEmpty()) {
            filters = filters.substring(0, filters.length - 1)
        }
        return filters
    }

    override fun getReasonList(): ArrayList<BadCsatReasonListItem> {
        return arguments?.getParcelableArrayList(PARAM_OPTIONS_CSAT) ?: ArrayList()
    }


    override fun onSuccessSubmit(intent: Intent) {
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun getCaption(): ArrayList<String> {
        return arguments?.getStringArrayList(CAPTION_LIST) ?: ArrayList()
    }

    override fun getQuestion(): ArrayList<String> {
        return arguments?.getStringArrayList(QUESTION_LIST) ?: ArrayList()
    }


    override fun showProgress() {
        progress = ProgressDialog(context)
        if (progress?.isShowing == false) {
            progress?.setMessage(UPDATING)
            progress?.isIndeterminate = true
            progress?.setCanceledOnTouchOutside(false)
            progress?.show()
        }
    }

    override fun hideProgress() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
            progress = null
        }
    }

    private fun initView(view: View) {
        mTxtHelpTitle = view.findViewById(getTextHelpTitleId())
        mSmileLayout = view.findViewById(getSmilleLayoutId())
        mTxtSmileSelected = view.findViewById(getSmileSelectedId())
        mTxtFeedbackQuestion = view.findViewById(getFeedbackQuestionId())
        mTxtFinished = view.findViewById(getTextFinishedId())
        mFilterReview = view.findViewById(getFilterReviewId())
        mTxtFinished.setOnClickListener { onSubmitClick() }
    }

    open fun getTextHelpTitleId(): Int = R.id.txt_help_title
    open fun getSmilleLayoutId(): Int = R.id.smile_layout
    open fun getSmileSelectedId(): Int = R.id.txt_smile_selected
    open fun getFeedbackQuestionId(): Int = R.id.txt_feedback_question
    open fun getTextFinishedId(): Int = R.id.txt_finished
    open fun getFilterReviewId(): Int = R.id.filter_review

    fun handleSubmitButtonState() {
        if (mFilterReview.isAnyItemSelected && reviewLength !in minLength..maxLength) {
            enableSubmitButton()
        } else {
            disableSubmitButton()
        }
    }

    fun updateReviewLength(reviewTextLength: Int) {
        reviewLength = reviewTextLength
        handleSubmitButtonState()
    }

    override fun disableSubmitButton() {
        mTxtFinished.setTextColor(MethodChecker.getColor(context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400))
        mTxtFinished.isEnabled = false
    }

    override fun enableSubmitButton() {
        mTxtFinished.setTextColor(MethodChecker.getColor(context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
        mTxtFinished.isEnabled = true
    }

    override fun hideSubmitButton() {
        mTxtFinished.hide()
    }

    override fun showSubmitButton() {
        mTxtFinished.show()
    }

    fun initObserver() {
        initObserverOfScreenEmojiState()
    }

    private fun initObserverOfScreenEmojiState() {
        viewModel?.screenState?.let { emojiState ->
            observe(emojiState) { emoji ->
                when (emoji) {
                    is ZeroScreenState -> {
                        setFilterList(viewModel?.reasonList.orEmpty())
                        hideSubmitButton()
                    }
                    else -> {
                        setFilterList(viewModel?.reasonList.orEmpty())
                        clearAllOfSelectedReason()
                        showSubmitButton()
                        updateScreenState(emoji)
                    }
                }
            }
        }
    }

    private fun clearAllOfSelectedReason(){
        selectedOption.clear()
    }

    private fun updateScreenState(emojiScreenState: ScreenState) {
        clearEmoji()
        setFirstEmoji(emojiScreenState.getFirstEmoji())
        setSecondEmoji(emojiScreenState.getSecondEmoji())
        setThirdEmoji(emojiScreenState.getThirdEmoji())
        setFourthEmoji(emojiScreenState.getFourthEmoji())
        setFifthEmoji(emojiScreenState.getFifthEmoji())
        setMessage(emojiScreenState.getMessage())
        setMessageColor(emojiScreenState.getMessageColor())
        setQuestion(emojiScreenState.getQuestion())
        disableSubmitButton()
    }

    private fun onSubmitClick() {
        val intent = Intent()
        intent.putExtra(EMOJI_STATE, viewModel?.emojiState.orZero())
        intent.putExtra(SELECTED_ITEM, getSelectedItem())
        onSuccessSubmit(intent)
    }

}
