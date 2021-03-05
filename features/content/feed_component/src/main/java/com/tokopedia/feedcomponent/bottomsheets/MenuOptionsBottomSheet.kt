package com.tokopedia.feedcomponent.bottomsheets

import android.content.Context
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
    private var menuOptionsClickInterface: MenuOptionsClickInterface? = null
    private var followText: String = ""
    private var isRecommendedPost: Boolean = false

    companion object {
        fun newInstance(context: Context, @StringRes followText: Int, isRecommendedPost: Boolean = false): MenuOptionsBottomSheet {
            return MenuOptionsBottomSheet().apply {
                this.menuOptionsClickInterface = context as? MenuOptionsClickInterface
                this.followText = getString(followText)
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
            menuOptionsClickInterface?.onFollow()
        }
        report.setOnClickListener {
            menuOptionsClickInterface?.onReport()
        }
        not_interested.setOnClickListener {
            menuOptionsClickInterface?.onNotInterested()
        }
    }

    interface MenuOptionsClickInterface {
        fun onReport()
        fun onFollow()
        fun onNotInterested()
    }
}