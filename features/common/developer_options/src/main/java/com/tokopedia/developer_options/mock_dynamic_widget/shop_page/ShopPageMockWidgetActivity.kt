package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster

class ShopPageMockWidgetActivity : BaseActivity(), ShopPageMockWidgetAdapter.ShopPageMockWidgetViewHolder.Listener {
    companion object {
        private const val SHARED_PREF_NAME = "SHARED_PREF_SHOP_PAGE_MOCK_WIDGET"
        private const val SHARED_PREF_MOCK_WIDGET_DATA = "SHARED_PREF_MOCK_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_BMSM_WIDGET_DATA = "SHARED_PREF_MOCK_BMSM_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_PLAY_WIDGET_DATA = "SHARED_PREF_MOCK_PLAY_WIDGET_DATA"
        private const val SHARED_PREF_MOCK_LOTTIE_URL_DATA = "SHARED_PREF_MOCK_LOTTIE_URL_DATA"
        private val BMSM_PD_WIDGET_OFFERING_INFO_DATA_SOURCE = R.raw.bmsm_pd_widget_get_offering_info_mock_response
        private val BMSM_GWP_WIDGET_OFFERING_INFO_DATA_SOURCE = R.raw.bmsm_gwp_widget_get_offering_info_mock_response
        private val PLAY_WIDGET_DATA_SOURCE = R.raw.play_widget_mock_response
    }

    private val sharedPref by lazy {
        getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
    private val adapter by lazy {
        ShopPageMockWidgetAdapter(this, true)
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
        configRv()
        configButtonTemplateShopWidget()
        configButtonCustomShopWidget()
        configButtonClearShopWidgetMockData()
        renderListMockShopWidgetData()
    }

    private fun configRv() {
        val rv = findViewById<RecyclerView>(R.id.rv_list_mock_shop_widget)
        rv.adapter = adapter
    }

    private fun configButtonCustomShopWidget() {
        findViewById<View>(R.id.shop_widget_mock_button_custom).setOnClickListener {
            showBottomSheetChooseCustomMockShopWidget()
        }
    }

    private fun showBottomSheetChooseCustomMockShopWidget() {
        val bottomSheet = ShopPageCustomMockWidgetBottomSheet.createInstance()
        bottomSheet.setOnAddSelectedShopWidget {
            addShopWidgetMockData(it)
            updateShopWidgetMockDataSharedPref()
        }
        bottomSheet.show(supportFragmentManager, "")
    }

    private fun addShopWidgetMockData(listShopPageMockWidgetModel: List<ShopPageMockWidgetModel>) {
        hideEmptyState()
        adapter.addMockWidgetModel(listShopPageMockWidgetModel)
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
            showBottomSheetChooseTemplateMockShopWidget()
        }
    }

    private fun showBottomSheetChooseTemplateMockShopWidget() {
        val bottomSheet = ShopPageTemplateMockWidgetBottomSheet.createInstance()
        bottomSheet.setOnAddSelectedShopWidget { listShopPageMockWidgetModel, lottieUrl ->
            addShopWidgetMockData(listShopPageMockWidgetModel)
            updateShopWidgetMockDataSharedPref()
            saveLottieUrl(lottieUrl)
        }
        bottomSheet.show(supportFragmentManager, "")
    }

    private fun renderListMockShopWidgetData() {
        val stringShopPageMockWidgetData = sharedPref.getString(SHARED_PREF_MOCK_WIDGET_DATA, null)
        if (null != stringShopPageMockWidgetData) {
            hideEmptyState()
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

    private fun updateShopWidgetMockDataSharedPref() {
        val listMockShopWidgetData = adapter.getData()
        ShopPageMockWidgetModelMapper.updateWidgetId(listMockShopWidgetData)
        ShopPageMockWidgetModelMapper.listMockWidgetDataToJson(listMockShopWidgetData.map { it.getMockShopWidgetData() })?.let {
            sharedPref.edit().putString(SHARED_PREF_MOCK_WIDGET_DATA, it).apply()
        }
        listMockShopWidgetData.forEach {
            when (it.getWidgetType()) {
                "group_offering_product" -> {
                    if (it.getWidgetName().equals("bmgm_banner_group", true)) {
                        ShopPageMockWidgetModelMapper.bmsmWidgetMockDataToJson(generateBmsmPdWidgetMockResponse().mockBmsmWidgetData)?.let {
                            sharedPref.edit().putString(SHARED_PREF_MOCK_BMSM_WIDGET_DATA, it).apply()
                        }
                    } else {
                        ShopPageMockWidgetModelMapper.bmsmWidgetMockDataToJson(generateBmsmGwpWidgetMockResponse().mockBmsmWidgetData)?.let {
                            sharedPref.edit().putString(SHARED_PREF_MOCK_BMSM_WIDGET_DATA, it).apply()
                        }
                    }
                }
                "dynamic" -> {
                    if (it.getWidgetName().equals("play", true)) {
                        resources.let {
                            ShopPageMockWidgetModelMapper.getStringMockWidgetJsonFromRaw(it, PLAY_WIDGET_DATA_SOURCE).let {
                                sharedPref.edit().putString(SHARED_PREF_MOCK_PLAY_WIDGET_DATA, it).apply()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveLottieUrl(url: String) {
        sharedPref.edit().putString(SHARED_PREF_MOCK_LOTTIE_URL_DATA, url).apply()
    }

    override fun onMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel) { }

    override fun onClearMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
        adapter.removeSelectedMockWidget(shopPageMockWidgetModel)
        updateShopWidgetMockDataSharedPref()
        if (adapter.getData().isEmpty()) {
            showEmptyState()
        }
    }

    private fun generateBmsmPdWidgetMockResponse(): BmsmMockWidgetModel {
        return resources?.let {
            ShopPageMockWidgetModelMapper.generateMockBmsmWidgetData(it, BMSM_PD_WIDGET_OFFERING_INFO_DATA_SOURCE)
        } ?: BmsmMockWidgetModel()
    }

    private fun generateBmsmGwpWidgetMockResponse(): BmsmMockWidgetModel {
        return resources?.let {
            ShopPageMockWidgetModelMapper.generateMockBmsmWidgetData(it, BMSM_GWP_WIDGET_OFFERING_INFO_DATA_SOURCE)
        } ?: BmsmMockWidgetModel()
    }
}
