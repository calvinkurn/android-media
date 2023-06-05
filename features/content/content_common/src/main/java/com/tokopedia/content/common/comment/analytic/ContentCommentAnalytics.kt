package com.tokopedia.content.common.comment.analytic

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.isPlay
import com.tokopedia.content.common.util.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3850
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/2772
 */
/**
 * @author by astidhiyaa on 21/03/23
 */
class ContentCommentAnalytics @AssistedInject constructor(
    @Assisted private val source: PageSource,
    @Assisted private val analyticModel: ContentCommentAnalyticsModel,
    private val userSessionInterface: UserSessionInterface
) : IContentCommentAnalytics {

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val userId: String
        get() = userSessionInterface.userId

    private val businessUnit: String
        get() = if (source.isPlay) KEY_BUSINESS_UNIT_PLAY else KEY_BUSINESS_UNIT_CONTENT

    override fun clickCommentIcon() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - comment button")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42591" else "41579")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun closeCommentSheet() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - close comment bottomsheet")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42592" else "41580")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickReplyChild() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - reply button")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42593" else "41581")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCommentName() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - name commenter")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42594" else "41582")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickProfilePicture() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - profile picture commenter")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42595" else "41583")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressLihatBalasan() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_CONTENT_IRIS)
            .setEventAction("view - lihat x balasan")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42596" else "41584")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickLihatBalasan() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - lihat x balasan")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42597" else "41585")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSembunyikan() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - sembunyikan comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42598" else "41586")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickTextBox() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - text box")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42599" else "41587")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSendParentComment() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - send main comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42600" else "41588")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSendChildComment() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - send reply comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42601" else "41589")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun longClickComment() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - long press slide comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42602" else "41590")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRemoveComment() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - hapus comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42603" else "41591")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickReportComment() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - laporkan comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42604" else "41592")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickReportReason(reportType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction("click - reason laporkan comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel("${analyticModel.eventLabel} - $reportType")
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42605" else "41593")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressSuccessReport() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_CONTENT_IRIS)
            .setEventAction("view - success report comment")
            .setEventCategory(analyticModel.eventCategory)
            .setEventLabel(analyticModel.eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, if (source.isPlay) "42606" else "41594")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(KEY_MARKETPLACE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    @AssistedFactory
    interface Creator {
        fun create(
            source: PageSource,
            model: ContentCommentAnalyticsModel
        ): ContentCommentAnalytics
    }
}
