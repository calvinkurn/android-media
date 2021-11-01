package com.tokopedia.mvcwidget.views.followViews

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.views.FollowCardView
import com.tokopedia.mvcwidget.views.TYPE_SMALL
import com.tokopedia.promoui.common.dpToPx

class MvcTokomemberFollowOneActionView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FollowCardView(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_view
    private var tvTitle: TextView
    var tvBtn: TextView
    var ll_btn: View

    init {
        View.inflate(context, layout, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvBtn = findViewById(R.id.btn_action_mvc_view)
        ll_btn = findViewById(R.id.ll_btn)
        radius = dpToPx(8)
        type = TYPE_SMALL
    }

    fun setData(followWidget: FollowWidget) {
        followWidget.content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvTitle.text = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
            } else {
                tvTitle.text = Html.fromHtml(it)
            }
        }
    }
}