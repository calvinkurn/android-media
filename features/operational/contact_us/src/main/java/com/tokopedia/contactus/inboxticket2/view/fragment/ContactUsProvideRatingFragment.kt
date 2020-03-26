package com.tokopedia.contactus.inboxticket2.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.contactus.R
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating

class ContactUsProvideRatingFragment: BaseFragmentProvideRating(){

    private lateinit var mBackButton: TextView

    companion object {
        fun newInstance(bundle: Bundle?): ContactUsProvideRatingFragment {
            val fragment = ContactUsProvideRatingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rating_provide, container, false)
        mBackButton = view.findViewById(R.id.toolbar_textview)
        setButtonListener()
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setButtonListener() {
        mBackButton.setOnTouchListener { _, event ->
            val drawableLeft = 0
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX <= mBackButton.compoundDrawables[drawableLeft].bounds.width() + mBackButton.paddingLeft) {
                    activity?.finish()
                }
            }
           true
        }
    }


    override fun getTextHelpTitleId():Int = R.id.txt_help_title
    override fun getSmilleLayoutId():Int = R.id.smile_layout
    override fun getSmileSelectedId():Int = R.id.txt_smile_selected
    override fun getFeedbackQuestionId():Int = R.id.txt_feedback_question
    override fun getTextFinishedId():Int = R.id.txt_finished
    override fun getFilterReviewId():Int = R.id.filter_review



}