package com.tokopedia.contactus.inboxticket2.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.view.viewModel.ContactUsRatingViewModel
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class ContactUsProvideRatingActivity : BaseSimpleActivity() {

    private val bottomSheetPage = BottomSheetUnify()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ContactUsRatingViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(ContactUsRatingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        val viewBottomSheetPage = initBottomSheet()

        bottomSheetPage.apply {
            showCloseIcon = false
            setChild(viewBottomSheetPage)
            showKnob = true
            clearContentPadding = true
            isFullpage = true
        }

        supportFragmentManager.let {
            bottomSheetPage.show(it, "")
        }
    }

    private fun initBottomSheet(): View? {
        val view = View.inflate(this, R.layout.bottom_sheet_rating_provide, null).apply {

        }
        return view
    }

    private fun initInjector() {
//        val inboxComponent = DaggerInboxComponent.builder().baseAppComponent(
//            (application as BaseMainApplication).baseAppComponent
//        )
//            .contact(InboxModule(this))
//            .build()
//
//        inboxComponent.inject(this)
    }

//    private fun initViewModel() {
//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(BaseProvideRatingFragmentViewModel::class.java)
//    }


    companion object {
        const val CLICKED_EMOJI = "clicked_emoji"
        const val PARAM_COMMENT_ID = "comment_id"
        const val PARAM_OPTIONS_CSAT = "options_csat"
        val CAPTION_LIST = "captionList"
        val QUESTION_LIST = "questionList"
        val CSAT_TITLE = "csatTitle"


        fun getInstance(context: Context, clickEmoji: Int, commentId: String, badCsatReasonListItems: ArrayList<BadCsatReasonListItem>): Intent {
            val intent = Intent(context, ContactUsProvideRatingActivity::class.java)
            intent.putExtra(CLICKED_EMOJI, clickEmoji)
            intent.putExtra(PARAM_COMMENT_ID, commentId)
            intent.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, badCsatReasonListItems)
            intent.putExtra(CAPTION_LIST, getCaption(context))
            intent.putExtra(QUESTION_LIST, getQuestion(context))
            intent.putExtra(CSAT_TITLE, context.resources.getString(R.string.text_satisfied))
            return intent
        }

        private fun getQuestion(context: Context): ArrayList<String> {
            val array = context.resources.getStringArray(R.array.contactus_csat_question)
            return arrayListOf(*array)
        }

        private fun getCaption(context: Context): ArrayList<String> {
            val array = context.resources.getStringArray(R.array.contactus_csat_caption)
            return arrayListOf(*array)
        }

    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}
