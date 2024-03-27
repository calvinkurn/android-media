package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.mock_dynamic_widget.shop_page.ShopPageMockWidgetModel.BmsmMockWidgetModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster

class ShopPageMockWidgetActivity : BaseActivity() {
    companion object {
        private const val SHARED_PREF_NAME = "SHARED_PREF_SHOP_PAGE_MOCK_WIDGET"
        private const val SHARED_PREF_MOCK_WIDGET_DATA = "SHARED_PREF_MOCK_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_BMSM_WIDGET_DATA = "SHARED_PREF_MOCK_BMSM_WIDGET_DATA"
    }

    private val sharedPref by lazy {
        getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val adapter by lazy {
        ShopPageMockWidgetAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalConfig.isAllowDebuggingTools()) {
            setContentView(R.layout.activity_shop_page_mock_dynamic_widget)
            setupView()
        } else {
            finish()
        }
    }

    private fun setupView() {
        configButtonRouteToShopPage()
        configButtonTemplateShopWidget()
        configButtonCustomShopWidget()
        configButtonClearShopWidgetMockData()
        renderListMockShopWidgetData()
    }

    private fun configButtonCustomShopWidget() {
        findViewById<View>(R.id.shop_widget_mock_button_custom).setOnClickListener {
        }
    }

    private fun configButtonClearShopWidgetMockData() {
        findViewById<View>(R.id.shop_widget_mock_button_clear).setOnClickListener {
            sharedPref.edit().clear().apply()
            renderListMockShopWidgetData()
        }
    }

    private fun configButtonRouteToShopPage() {
        findViewById<View>(R.id.route_to_shop_page_button).setOnClickListener {
            val shopId = findViewById<TextFieldUnify>(R.id.shop_id_text).textFieldInput.text.toString()
            if (shopId.isNotEmpty()) {
                RouteManager.route(this, "tokopedia://shop/$shopId")
            } else {
                Toaster.build(findViewById(android.R.id.content), "Please input shop id").show()
            }
        }
    }

    private fun configButtonTemplateShopWidget() {
        findViewById<View>(R.id.shop_widget_mock_button_template).setOnClickListener {
            showBottomSheetChooseTemplateShopWidget()
        }
    }

    private fun showBottomSheetChooseTemplateShopWidget() {
        val bottomSheet = ShopPageTemplateWidgetBottomSheet.createInstance()
        bottomSheet.setOnOptionSelected {
            addShopWidgetMockDataToSharedPref(it)
            renderListMockShopWidgetData()
        }
        bottomSheet.show(supportFragmentManager, "")
    }

    private fun renderListMockShopWidgetData() {
        val stringShopPageMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null)
        if (null != stringShopPageMockWidgetData) {
            hideEmptyState()
            val rv = findViewById<RecyclerView>(R.id.rv_list_mock_shop_widget)
            rv.adapter = adapter
            val listShopPageMockData = ShopPageMockWidgetModelMapper.mapToShopPageMockWidgetModel(stringShopPageMockWidgetData)
            adapter.setListShopPageMockWidget(listShopPageMockData)
        } else {
            showEmptyState()
        }
    }

    private fun hideEmptyState() {
        findViewById<View>(R.id.rv_list_mock_shop_widget).show()
        findViewById<View>(R.id.text_empty_state).hide()
    }

    private fun showEmptyState() {
        findViewById<View>(R.id.rv_list_mock_shop_widget).hide()
        findViewById<View>(R.id.text_empty_state).show()
    }

    private fun addShopWidgetMockDataToSharedPref(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
        val mockShopWidgetData = if (sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null) != null) {
            val stringShopPageMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null)
            val listShopPageMockData = ShopPageMockWidgetModelMapper.mapToShopPageMockWidgetModel(stringShopPageMockWidgetData.orEmpty())
            listShopPageMockData.toMutableList().apply {
                add(shopPageMockWidgetModel)
            }.toList()
        } else {
            listOf(shopPageMockWidgetModel)
        }
        ShopPageMockWidgetModelMapper.updateWidgetId(mockShopWidgetData)
        ShopPageMockWidgetModelMapper.listMockWidgetDataToJson(mockShopWidgetData.map { it.getMockShopWidgetData() })?.let {
            sharedPref.edit().putString(SHARED_PREF_MOCK_WIDGET_DATA, it).apply()
        }
        if (shopPageMockWidgetModel.getWidgetType().equals("group_offering_product", true)) {
            if (shopPageMockWidgetModel.getWidgetName().equals("bmgm_banner_group", true)) {
                ShopPageMockWidgetModelMapper.bmsmWidgetMockDataToJson(generateBmsmPdWidgetMockResponse().mockBmsmWidgetData)?.let {
                    sharedPref.edit().putString(SHARED_PREF_MOCK_BMSM_WIDGET_DATA, it).apply()
                }
            } else {
                ShopPageMockWidgetModelMapper.bmsmWidgetMockDataToJson(generateBmsmGwpWidgetMockResponse().mockBmsmWidgetData)?.let {
                    sharedPref.edit().putString(SHARED_PREF_MOCK_BMSM_WIDGET_DATA, it).apply()
                }
            }
        }
    }

    private fun generateBmsmPdWidgetMockResponse(): BmsmMockWidgetModel {
        return resources?.let {
            val offeringInfoMockJsonData = ShopPageMockWidgetModelMapper.getBmsmPdWidgetOfferingInfoMockJsonFromRaw(it)
            val offeringProductMockJsonData = ShopPageMockWidgetModelMapper.getBmsmWidgetOfferingProductMockJsonFromRaw(it)
            ShopPageMockWidgetModelMapper.generateMockBmsmWidgetData(offeringInfoMockJsonData, offeringProductMockJsonData)
        } ?: BmsmMockWidgetModel()
    }

    private fun generateBmsmGwpWidgetMockResponse(): BmsmMockWidgetModel {
        return resources?.let {
            val offeringInfoMockJsonData = ShopPageMockWidgetModelMapper.getBmsmGwpWidgetOfferingInfoMockJsonFromRaw(it)
            val offeringProductMockJsonData = ShopPageMockWidgetModelMapper.getBmsmWidgetOfferingProductMockJsonFromRaw(it)
            ShopPageMockWidgetModelMapper.generateMockBmsmWidgetData(offeringInfoMockJsonData, offeringProductMockJsonData)
        } ?: BmsmMockWidgetModel()
    }
}
