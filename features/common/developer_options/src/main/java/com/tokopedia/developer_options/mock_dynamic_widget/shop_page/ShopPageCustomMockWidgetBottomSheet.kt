package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.developer_options.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ShopPageCustomMockWidgetBottomSheet : BottomSheetUnify() {

    private var onAddSelectedWidget: (List<ShopPageMockWidgetModel>) -> Unit = {}
    private val childLayoutRes = R.layout.shop_page_custom_mock_widget_bottom_sheet_layout
    private val isGenerateDynamicTabWidgetResponse: Boolean
        get() = view?.findViewById<CheckboxUnify>(R.id.toggle_is_generate_dynamic_data_widget_response)?.isChecked.orFalse()
    private val dynamicWidgetMockResponse: String
        get() = view?.findViewById<TextAreaUnify2>(R.id.text_area_dynamic_tab_widget_response)?.editText?.text?.toString().orEmpty()
    private val layoutV2WidgetMockResponse: String
        get() = view?.findViewById<TextAreaUnify2>(R.id.text_area_layout_v2_widget_response)?.editText?.text?.toString().orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isHideable = false
        showCloseIcon = true
        showHeader = true
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTextArea()
        configToggleGenerateDynamicTabResponse()
        configButtonAddCustomWidget()
    }

    private fun configToggleGenerateDynamicTabResponse() {
        view?.findViewById<CheckboxUnify>(R.id.toggle_is_generate_dynamic_data_widget_response)?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                view?.findViewById<TextAreaUnify2>(R.id.text_area_dynamic_tab_widget_response)?.editText?.setText("")
                view?.findViewById<TextAreaUnify2>(R.id.text_area_dynamic_tab_widget_response)?.isEnabled = false
            } else {
                view?.findViewById<TextAreaUnify2>(R.id.text_area_dynamic_tab_widget_response)?.isEnabled = true
            }
        }

    }

    private fun generateDynamicTabMockResponseFromV2Response(layoutV2MockResponseJsonObject: JsonObject): JsonObject {
        return ShopPageMockWidgetModelMapper.generateMockDynamicTabData(layoutV2MockResponseJsonObject)
    }

    private fun configTextArea() {
        view?.findViewById<TextAreaUnify2>(R.id.text_area_dynamic_tab_widget_response)?.apply {
            minLine = 20
            maxLine = 20

        }
        view?.findViewById<TextAreaUnify2>(R.id.text_area_layout_v2_widget_response)?.apply {
            minLine = 20
            maxLine = 20
        }
    }

    private fun configButtonAddCustomWidget() {
        view?.findViewById<UnifyButton>(R.id.btn_add_custom_shop_widget)?.setOnClickListener {
            val layoutV2MockResponseJsonObject: JsonObject = try {
                JsonParser.parseString(layoutV2WidgetMockResponse).asJsonObject
            } catch (e: Exception) {
                Toaster.build(it.rootView, "Layout V2 mock response error: ${e.message}").show()
                return@setOnClickListener
            }
            if(!isValidLayoutV2MockResponse(layoutV2MockResponseJsonObject)){
                Toaster.build(it.rootView, "Wrong layout V2 mock response").show()
                return@setOnClickListener
            }
            val dynamicTabMockResponseJsonObject: JsonObject = try {
                if (isGenerateDynamicTabWidgetResponse) {
                    generateDynamicTabMockResponseFromV2Response(layoutV2MockResponseJsonObject)
                } else {
                    JsonParser.parseString(dynamicWidgetMockResponse).asJsonObject
                }
            } catch (e: Exception) {
                Toaster.build(it.rootView, "Dynamic Tab mock response error: ${e.message}").show()
                return@setOnClickListener
            }
            if(!isValidDynamicTabMockResponse(dynamicTabMockResponseJsonObject)){
                Toaster.build(it.rootView, "Wrong layout V2 mock response").show()
                return@setOnClickListener
            }
            onAddSelectedWidget.invoke(listOf(
                ShopPageMockWidgetModel(
                    Pair(
                        dynamicTabMockResponseJsonObject.toString(),
                        layoutV2MockResponseJsonObject.toString(),
                    )
                ).apply {
                    markAsCustomWidget()
                }
            ))
            dismiss()
        }
    }

    private fun isValidLayoutV2MockResponse(jsonObj: JsonObject): Boolean {
        return jsonObj.has("type") && jsonObj.has("name")
    }

    private fun isValidDynamicTabMockResponse(jsonObj: JsonObject): Boolean {
        return jsonObj.has("widgetType") && jsonObj.has("widgetName")
    }


    companion object {
        const val TITLE = "Add Custom Shop Widget"
        fun createInstance(): ShopPageCustomMockWidgetBottomSheet {
            return ShopPageCustomMockWidgetBottomSheet()
        }
    }

    fun setOnAddSelectedShopWidget(onAddSelectedWidget: (List<ShopPageMockWidgetModel>) -> Unit) {
        this.onAddSelectedWidget = onAddSelectedWidget
    }

}
