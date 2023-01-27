package com.tokopedia.csat_rating.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.csat_rating.R
import com.tokopedia.csat_rating.adapter.chatbot_csat.ChatBotQuickOptionViewCsat
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.di.component.DaggerCsatComponent
import com.tokopedia.csat_rating.di.module.CsatRatingModule
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject


open class BottomSheetProvideRatingActivity : BaseSimpleActivity() {

    private val bottomSheetPage = BottomSheetUnify()
    private var selectedOption: MutableList<String> = ArrayList()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: BaseProvideRatingFragmentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(BaseProvideRatingFragmentViewModel::class.java)
    }

    private var csatTitle: com.tokopedia.unifyprinciples.Typography? = null
    private var emojiLayout: LinearLayout? = null
    private var textEmojiSelected: com.tokopedia.unifyprinciples.Typography? = null
    private var feedbackQuestion: com.tokopedia.unifyprinciples.Typography? = null
    private var filterReview: ChatBotQuickOptionViewCsat? = null
    private var buttonFinished: com.tokopedia.unifyprinciples.Typography? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_csat_rating)
        initInjector()
        initObserver()
        getValuesFromIntent()

        val viewBottomSheetPage = initBottomSheet()

        bottomSheetPage.apply {
            showCloseIcon = false
            setChild(viewBottomSheetPage)
            showKnob = true
            clearContentPadding = true
            isFullpage = true
            setOnDismissListener {
                finish()
            }
        }
        supportFragmentManager.let {
            bottomSheetPage.show(it, "")
        }
    }

    private fun getValuesFromIntent() {
        val caption =
            intent.getStringArrayListExtra(BaseFragmentProvideRating.CAPTION_LIST).orEmpty()
        viewModel.setCaption(caption as ArrayList<String>)
        val question =
            intent.getStringArrayListExtra(BaseFragmentProvideRating.QUESTION_LIST).orEmpty()
        viewModel.setQuestion(question as ArrayList<String>)
        val reasonItemList: ArrayList<BadCsatReasonListItem> = intent?.getParcelableArrayListExtra(
            BaseFragmentProvideRating.PARAM_OPTIONS_CSAT
        ) ?: ArrayList()
        viewModel.setReasonList(reasonItemList)
        val emojiState = intent?.getIntExtra(BaseFragmentProvideRating.CLICKED_EMOJI, 0)
            ?: BaseFragmentProvideRating.NO_EMOJI
        viewModel.setSelectedEmoji(emojiState)
        viewModel.setCsatTitle(intent?.getStringExtra(BaseFragmentProvideRating.CSAT_TITLE) ?: "")
    }

    private fun initBottomSheet(): View? {
        val view = View.inflate(this, R.layout.bottom_sheet_csat, null).apply {
            csatTitle = findViewById(R.id.rating_txt_help_title)
            emojiLayout = findViewById(R.id.smile_layout)
            textEmojiSelected = findViewById(R.id.txt_smile_selected)
            feedbackQuestion = findViewById(R.id.txt_feedback_question)
            filterReview = findViewById(R.id.filter_review)
            buttonFinished = findViewById(R.id.txt_finished)
            buttonFinished?.setOnClickListener {
                onSubmitButton()
            }

            csatTitle?.text = viewModel.csatTitle
        }
        return view
    }

    private fun initInjector() {

        DaggerCsatComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        )
            .csatRatingModule(CsatRatingModule(this))
            .build()
            .inject(this)
    }

    private fun initObserver() {
        initObserverOfScreenEmojiState()
    }

    private fun initObserverOfScreenEmojiState() {
        viewModel.screenState.let { emojiState ->
            observe(emojiState) { emoji ->
                when (emoji) {
                    is ZeroScreenState -> {
                        setFilterList(viewModel.reasonList)
                        hideSubmitButton()
                    }
                    else -> {
                        setFilterList(viewModel.reasonList)
                        clearAllOfSelectedReason()
                        showSubmitButton()
                        updateScreenState(emoji)
                    }
                }
            }
        }
    }

    private fun hideSubmitButton() {
        buttonFinished?.hide()
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

    private fun setFirstEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener {
            viewModel.setSelectedEmoji(
                FIRST_EMOJI
            )
        }
    }

    private fun disableSubmitButton() {
        buttonFinished?.setTextColor(
            MethodChecker.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_NN400
            )
        )
        buttonFinished?.isEnabled = false
    }

    private fun setSecondEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener {
            viewModel.setSelectedEmoji(
                SECOND_EMOJI
            )
        }
    }

    private fun setThirdEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener {
            viewModel.setSelectedEmoji(
                THIRD_EMOJI
            )
        }
    }

    private fun setFourthEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener {
            viewModel.setSelectedEmoji(
                FOURTH_EMOJI
            )
        }
    }

    private fun setFifthEmoji(drawable: Int) {
        addImageView(drawable).setOnClickListener {
            viewModel.setSelectedEmoji(
                FIFTH_EMOJI
            )
        }
    }

    private fun setMessage(message: String) {
        textEmojiSelected?.text = message

    }

    private fun setMessageColor(color: Int) {
        textEmojiSelected?.setTextColor(MethodChecker.getColor(this, color))
    }

    private fun setQuestion(question: String) {
        feedbackQuestion?.text = question
    }

    private fun addImageView(drawable: Int): ImageView {
        val imageView = ImageView(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.weight = 1.0f
        imageView.setImageResource(drawable)
        imageView.layoutParams = layoutParams
        emojiLayout?.addView(imageView)
        return imageView
    }

    private fun clearEmoji() {
        emojiLayout?.removeAllViews()
    }

    private fun clearAllOfSelectedReason() {
        selectedOption.clear()
    }

    private fun showSubmitButton() {
        buttonFinished?.show()
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    fun setFilterList(filterList: List<BadCsatReasonListItem>) {
        filterReview?.updateLayoutManager(getLayoutManager())
        val filterItems = ArrayList<QuickFilterItem>()
        var finishFilter: QuickFilterItem? = null
        for (filter in filterList) {
            finishFilter = QuickFilterItem()
            finishFilter.name = filter.message
            finishFilter.type = filter.id.toString()
            finishFilter.setColorBorder(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            filterItems.add(finishFilter)
        }
        filterReview?.renderFilter(filterItems)
        filterReview?.setListener(object : QuickSingleFilterView.ActionListener {
            override fun selectFilter(typeFilter: String?) {
                if (selectedOption.contains(typeFilter)) {
                    selectedOption.remove(typeFilter)
                } else {
                    if (!(filterList.isNotEmpty() && filterList[0].id > 0)) {
                        //Neglect
                    }
                    if (typeFilter != null) {
                        selectedOption.add(typeFilter)
                    }
                }
                handleSubmitButtonState()
            }
        })
    }

    fun handleSubmitButtonState() {
        if (filterReview?.isAnyItemSelected == true) {
            enableSubmitButton()
        } else {
            disableSubmitButton()
        }
    }

    private fun enableSubmitButton() {
        buttonFinished?.setTextColor(
            MethodChecker.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )
        buttonFinished?.isEnabled = true
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    open fun onSubmitButton() {
        val intent = Intent()
        intent.putExtra(BaseFragmentProvideRating.EMOJI_STATE, viewModel.emojiState.orZero())
        intent.putExtra(BaseFragmentProvideRating.SELECTED_ITEM, getSelectedItem())
        onSuccessSubmit(intent)
    }

    open fun onSuccessSubmit(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun getSelectedItem(): String {
        var filters = ""
        for (filter in selectedOption) {
            filters += "$filter;"
        }
        if (filters.isNotEmpty()) {
            filters = filters.substring(0, filters.length - 1)
        }
        return filters
    }

}
