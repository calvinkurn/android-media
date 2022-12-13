package feedcomponent.model

import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse

/**
 * Created by shruti agarwal on 05/12/22.
 */

class FeedMVCModelBuilder {

    fun getMVCResponseSuccess(
        data: TokopointsCatalogMVCSummary? = buildTokopointsCatalogMVCSummary()
    ) = TokopointsCatalogMVCSummaryResponse(data = data)

    fun buildTokopointsCatalogMVCSummary(): TokopointsCatalogMVCSummary = TokopointsCatalogMVCSummary(
        resultStatus = ResultStatus(
            code = "200",
            message = emptyList(),
            status = "",
            reason = ""
        ),
        animatedInfoList = emptyList(),
        counterTotal = 0,
        isShown = false
    )
    fun getMVCResponseSuccessWithResponseNot200(
        data: TokopointsCatalogMVCSummary? = buildTokopointsCatalogMVCSummaryWithResponseMsg()
    ) = TokopointsCatalogMVCSummaryResponse(data = data)

    private fun buildTokopointsCatalogMVCSummaryWithResponseMsg(): TokopointsCatalogMVCSummary = TokopointsCatalogMVCSummary(
        resultStatus = ResultStatus(
            code = "500",
            message = listOf(""),
            status = "",
            reason = ""
        ),
        animatedInfoList = emptyList(),
        counterTotal = 0,
        isShown = false
    )

    fun buildTokopointsCatalogMVCSummaryWithResponseNotResponseAndMsgNull(): TokopointsCatalogMVCSummary = TokopointsCatalogMVCSummary(
        resultStatus = ResultStatus(
            code = "500",
            message = null,
            status = "",
            reason = ""
        ),
        animatedInfoList = emptyList(),
        counterTotal = 0,
        isShown = false
    )

    fun buildTokopointsCatalogMVCSummaryWithResultStatusNull(): TokopointsCatalogMVCSummary = TokopointsCatalogMVCSummary(
        resultStatus = null,
        animatedInfoList = emptyList(),
        counterTotal = 0,
        isShown = false
    )
}
