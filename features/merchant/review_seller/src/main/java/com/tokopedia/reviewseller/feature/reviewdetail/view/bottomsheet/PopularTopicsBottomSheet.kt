package com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import android.view.View.inflate
import androidx.fragment.app.FragmentActivity
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger
import com.tokopedia.reviewseller.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Yehezkiel on 27/04/20
 */
class PopularTopicsBottomSheet(val mActivity: FragmentActivity?, val data:String, val listener: (List<String>) -> Unit) : BottomSheetUnify() {

    init {
        val contentView = View.inflate(mActivity, R.layout.dialog_popular_topics, null)
        setChild(contentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.dialog_check_imei_btn.setOnClickListener {
//            onUnderstandClicked()
//            dismiss()
//        }
    }


    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, "todo rizqi")
        }
    }
    override fun dismiss() {
        super.dismiss()
        listener.invoke(listOf())
    }
}