package com.tokopedia.globalnavwidget

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.tokopedia.globalnavwidget.test.R

class GlobalNavWidgetActivity : AppCompatActivity(), GlobalNavWidgetListener {

    private val constraintLayoutGlobalNavWidgetActivity by lazy {
        findViewById<ConstraintLayout>(R.id.constraintLayoutGlobalNavWidgetActivity)
    }

    private val globalNavWidgetWithNoInfo by lazy {
        findViewById<GlobalNavWidget>(R.id.globalNavWidgetWithNoInfo)
    }

    private val globalNavWidgetWithInfo by lazy {
        findViewById<GlobalNavWidget>(R.id.globalNavWidgetWithNoInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_nav_widget)

        initGlobalNavWidgetWithNoInfo()
        initGlobalNavWidgetWithInfo()
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
        val globalNavWidgetModel = buildGlobalNavModel(globalNavWidgetItemNoInfo)

        globalNavWidgetWithNoInfo.setData(globalNavWidgetModel, this)
    }

    private fun initGlobalNavWidgetWithInfo() {
        val globalNavWidgetModel = buildGlobalNavModel(globalNavWidgetItemWithInfo)

        globalNavWidgetWithInfo.setData(globalNavWidgetModel, this)
    }
}
