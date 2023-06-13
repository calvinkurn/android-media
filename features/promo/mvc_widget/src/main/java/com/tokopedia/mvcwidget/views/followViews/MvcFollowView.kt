package com.tokopedia.mvcwidget.views.followViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.mvcwidget.FollowWidget
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.views.WidgetImpression
import com.tokopedia.user.session.UserSession

class MvcFollowViewContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.mvc_tokomember_follow_container
    var oneActionView: MvcTokomemberFollowOneActionView
    var twoActionView: MvcTokomemberFollowTwoActionsView
    private var divider: View

    init {
        View.inflate(context, layout, this)
        oneActionView = findViewById(R.id.one_action_view)
        twoActionView = findViewById(R.id.two_action_view)
        divider = findViewById(R.id.divider)
        orientation = VERTICAL
    }

    fun setData(followWidget: FollowWidget, widgetImpression: WidgetImpression, shopId: String, @MvcSource source: Int, mvcTracker:MvcTracker?) {
        divider.visibility = View.GONE

        if (followWidget.isShown == true) {
            followWidget.type?.let {
                when (it) {
                    FollowWidgetType.FIRST_FOLLOW -> {
                        oneActionView.visibility = View.VISIBLE
                        twoActionView.visibility = View.GONE
                        oneActionView.setData(followWidget)
                        divider.visibility = View.VISIBLE

                        if (!widgetImpression.sentFollowWidgetImpression) {
                            mvcTracker?.viewWidgetImpression(FollowWidgetType.FIRST_FOLLOW, shopId, UserSession(context).userId, source)
                            widgetImpression.sentFollowWidgetImpression = true
                        }
                    }
                    FollowWidgetType.MEMBERSHIP_OPEN -> {
                        commonViewVisibility(followWidget,shopId,source,mvcTracker)

                        if (!widgetImpression.sentJadiMemberImpression) {
                            mvcTracker?.viewWidgetImpression(FollowWidgetType.MEMBERSHIP_OPEN, shopId, UserSession(context).userId, source)
                            widgetImpression.sentJadiMemberImpression = true
                        }
                    }
                    FollowWidgetType.MEMBERSHIP_CLOSE -> {
                        commonViewVisibility(followWidget,shopId,source,mvcTracker)

                        if (!widgetImpression.sentJadiMemberImpression) {
                            mvcTracker?.viewWidgetImpression(FollowWidgetType.MEMBERSHIP_CLOSE, shopId, UserSession(context).userId, source)
                            widgetImpression.sentJadiMemberImpression = true
                        }
                    }
                }
            }
        }
    }

    private fun commonViewVisibility(followWidget: FollowWidget, shopId: String, source: Int, mvcTracker: MvcTracker?) {
        twoActionView.visibility = View.VISIBLE
        oneActionView.visibility = View.GONE
        twoActionView.setData(followWidget, shopId, source, mvcTracker)
        divider.visibility = View.VISIBLE
    }

}
