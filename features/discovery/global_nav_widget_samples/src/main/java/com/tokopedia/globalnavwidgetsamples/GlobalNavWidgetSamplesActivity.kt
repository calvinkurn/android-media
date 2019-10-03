package com.tokopedia.globalnavwidgetsamples

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import kotlinx.android.synthetic.main.activity_global_nav_widget.*

class GlobalNavWidgetSamplesActivity : AppCompatActivity(), GlobalNavWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_nav_widget)

        globalNavWidgetWithNoInfo?.setData(globalNavWidgetModelItemNoInfo, this)
        globalNavWidgetWithInfo?.setData(globalNavWidgetModelItemWithInfo, this)
        globalNavWidgetCard?.setData(globalNavWidgetModelCard, this)
    }

    override fun onClickItem(item: GlobalNavWidgetModel.Item) {
        Snackbar.make(constraintLayoutGlobalNavWidget, item.clickItemApplink, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel) {
        Snackbar.make(constraintLayoutGlobalNavWidget, globalNavWidgetModel.clickSeeAllApplink, Snackbar.LENGTH_SHORT).show()
    }
}
