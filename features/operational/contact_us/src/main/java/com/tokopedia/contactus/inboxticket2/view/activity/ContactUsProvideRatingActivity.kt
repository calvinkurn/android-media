package com.tokopedia.contactus.inboxticket2.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.view.fragment.ContactUsProvideRatingFragment
import com.tokopedia.csat_rating.activity.BaseProvideRatingActivity
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

class ContactUsProvideRatingActivity : BaseProvideRatingActivity() {


    override fun getNewFragment(): Fragment {
        return ContactUsProvideRatingFragment.newInstance(intent.extras)
    }

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
}
