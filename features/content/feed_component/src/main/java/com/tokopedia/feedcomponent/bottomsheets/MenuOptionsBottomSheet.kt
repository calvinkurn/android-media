package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_menu_options.*

class MenuOptionsBottomSheet : BottomSheetUnify() {
    private var followText: String = ""
    private var isRecommendedPost: Boolean = false
    var onReport: (() -> Unit)? = null
    var onDeleteorFollow:(()->Unit)? = null

    companion object {
        fun newInstance(followText: String, isRecommendedPost: Boolean = false): MenuOptionsBottomSheet {
            return MenuOptionsBottomSheet().apply {
                this.followText = followText
                this.isRecommendedPost = isRecommendedPost
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_menu_options, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        not_interested.showWithCondition(isRecommendedPost)
        follow.text = followText
        follow.setOnClickListener {
            onDeleteorFollow?.invoke()
            dismiss()
        }
        report.setOnClickListener {
            onReport?.invoke()
            dismiss()
        }
    }
}