package com.tokopedia.globalnavwidgetsamples

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import kotlinx.android.synthetic.main.activity_global_nav_widget.*

class GlobalNavWidgetSamplesActivity : AppCompatActivity(), GlobalNavWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_nav_widget)

        initGlobalNavWidgetWithNoInfo()
        initGlobalNavWidgetWithInfo()
        initGlobalNavWidgetCard()
    }

    private fun buildGlobalNavModel(jsonString: String): GlobalNavWidgetModel {
        return Gson().fromJson<GlobalNavWidgetModel>(jsonString, GlobalNavWidgetModel::class.java)
    }

    override fun onClickItem(item: GlobalNavWidgetModel.Item) {
        Snackbar.make(constraintLayoutGlobalNavWidgetActivity, item.name, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel) {
        Snackbar.make(constraintLayoutGlobalNavWidgetActivity, globalNavWidgetModel.title, Snackbar.LENGTH_SHORT).show()
    }

    private fun initGlobalNavWidgetWithNoInfo() {
        val globalNavWidgetModel = buildGlobalNavModel(globalNavWidgetModelItemNoInfo)

        globalNavWidgetWithNoInfo?.setData(globalNavWidgetModel, this)
    }

    private fun initGlobalNavWidgetWithInfo() {
        val globalNavWidgetModel = buildGlobalNavModel(globalNavWidgetModelItemWithInfo)

        globalNavWidgetWithInfo?.setData(globalNavWidgetModel, this)
    }

    private fun initGlobalNavWidgetCard() {
        val globalNavWidgetModel = buildGlobalNavModel(globalNavWidgetModelCard)

        globalNavWidgetCard?.setData(globalNavWidgetModel, this)
    }
}
