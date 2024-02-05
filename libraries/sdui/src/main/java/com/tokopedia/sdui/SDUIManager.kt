package com.tokopedia.sdui

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sdui.extention.ActionHandler
import com.tokopedia.sdui.extention.GlideDivImageLoader
import com.tokopedia.sdui.extention.HTMLHandler
import com.tokopedia.sdui.extention.TypeFaceProvider
import com.tokopedia.sdui.interfaces.SDUITrackingInterface
import com.tokopedia.sdui.interfaces.SDUIinterface
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.lottie.DivLottieExtensionHandler
import com.yandex.div2.DivData
import org.json.JSONArray
import org.json.JSONObject

class SDUIManager : SDUIinterface {

    private var divContext: Div2Context? = null
    private var context: Context? = null
    private var parsingEnvironment: DivParsingEnvironment? = null
    override fun initSDUI(context: Context,
                          sduiTrackingInterface: SDUITrackingInterface?) {
        this.context = context
        initDivKit(context, sduiTrackingInterface)
    }

    private fun initDivKit(context: Context,
                           sduiTrackingInterface: SDUITrackingInterface? = null) {
        divContext = Div2Context(baseContext = context as ContextThemeWrapper,
            configuration = createDivConfiguration(context, sduiTrackingInterface))
    }

    private fun createDivConfiguration(context: Context,
                                       sduiTrackingInterface: SDUITrackingInterface? = null): DivConfiguration {
        return DivConfiguration.Builder(GlideDivImageLoader(context))
            .actionHandler(ActionHandler(context, sduiTrackingInterface))
            .extension(DivLottieExtensionHandler())
            .extension(HTMLHandler())
            .enableViewPool(false)
            .typefaceProvider(TypeFaceProvider(context))
            .supportHyphenation(true)
            .visualErrorsEnabled(true)
            .build()
    }

    override fun createView(
        context: Context,
        templateJson: JSONObject,
        cardsJsonObject: JSONObject,
        viewType: String
    ) : View? {
        parsingEnvironment = DivParsingEnvironment(ParsingErrorLogger.LOG).apply {
            parseTemplates(templateJson)
        }
        val divView = divContext?.let {
            Div2View(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
        return bindJsonToView(cardsJsonObject, divView, parsingEnvironment)
    }

    override fun createView(
        context: Context,
        templateJson: JSONObject,
        cardsJsonArray: JSONArray,
        viewType: String
    ) : View? {
        //Do not use yet
        return null
    }

    private fun bindJsonToView(cardJson: JSONObject, divView: Div2View?,
                               parsingEnvironment: DivParsingEnvironment?) : Div2View? {
        val divData = parsingEnvironment?.let { DivData(it, cardJson) }
        if (divData != null) {
            divView?.setData(divData, DivDataTag(divData.logId))
        }
        return divView
    }
}
