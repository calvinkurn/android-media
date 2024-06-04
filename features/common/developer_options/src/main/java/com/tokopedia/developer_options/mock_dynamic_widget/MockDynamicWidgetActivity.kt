package com.tokopedia.developer_options.mock_dynamic_widget

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.mock_dynamic_widget.shop_page.ShopPageMockWidgetActivity

class MockDynamicWidgetActivity : BaseActivity(), MockDynamicWidgetAdapter.MockDynamicWidgetPageViewHolder.Listener {

    private val mockDynamicWidgetAdapter: MockDynamicWidgetAdapter by lazy {
        MockDynamicWidgetAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalConfig.isAllowDebuggingTools()) {
            setContentView(R.layout.activity_mock_dynamic_widget_entry_page)
            setupView()
        } else {
            finish()
        }
    }

    private fun setupView() {
        configRvData()
    }

    private fun configRvData() {
        findViewById<RecyclerView>(R.id.rv_mock_dynamic_widget_entry_page).apply {
            this.adapter = mockDynamicWidgetAdapter
        }
        configEntryPageData()
    }

    private fun configEntryPageData() {
        //add your entry page here
        addShopPageEntryPage()
    }

    private fun addShopPageEntryPage() {
        addEntryPage(MockDynamicWidgetModel(
            "Shop Page",
            ShopPageMockWidgetActivity::class.java
        ))
    }

    private fun addEntryPage(model: MockDynamicWidgetModel) {
        mockDynamicWidgetAdapter.addEntryPage(model)
    }

    override fun onItemClick(model: MockDynamicWidgetModel) {
        val intent = Intent(this, model.pageClass)
        startActivity(intent)
    }

}
