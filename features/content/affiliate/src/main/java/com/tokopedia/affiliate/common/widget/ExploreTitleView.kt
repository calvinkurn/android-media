package com.tokopedia.affiliate.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.viewmodel.ExploreTitleViewModel
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.widget_af_affiliate_title.view.*

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreTitleView : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_af_affiliate_title, this)
    }

    fun bind(titleViewModel: ExploreTitleViewModel) {
        titleTv.shouldShowWithAction(!TextUtils.isEmpty(titleViewModel.title)) {
            titleTv.text = titleViewModel.title
        }
        subtitleTv.shouldShowWithAction(!TextUtils.isEmpty(titleViewModel.subtitle)) {
            subtitleTv.text = titleViewModel.subtitle
        }
    }

}