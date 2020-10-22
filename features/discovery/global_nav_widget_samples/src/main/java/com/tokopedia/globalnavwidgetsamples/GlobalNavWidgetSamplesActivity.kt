package com.tokopedia.globalnavwidgetsamples

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import kotlinx.android.synthetic.main.activity_global_nav_widget.*

class GlobalNavWidgetSamplesActivity : AppCompatActivity(), GlobalNavWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_nav_widget)

        globalNavWidgetPillWithNoInfo?.setData(globalNavWidgetPillModelItemNoInfo, this)
        globalNavWidgetPillWithInfo?.setData(globalNavWidgetPillModelItemWithInfo, this)
        globalNavWidgetPillWithoutSeeAllApplink?.setData(globalNavWidgetPillModelWithoutSeeAllApplink, this)
        globalNavWidgetWithoutBackground?.setData(globalNavWidgetWithoutBackgroundInModel, this)
        globalNavWidgetCard?.setData(globalNavWidgetModelCard, this)
        globalNavWidgetCard2?.setData(globalNavWidgetModelCard2, this)
        singleGlobalNavWidgetPill?.setData(singleGlobalNavWidgetModelPill, this)
        singleGlobalNavWidgetCardIconWithNoSubtitleInfo?.setData(singleGlobalNavWidgetModelCardIconWithNoSubtitleInfo, this)
        singleGlobalNavWidgetCardIcon?.setData(singleGlobalNavWidgetModelCardIcon, this)
        singleGlobalNavWidgetCardImage?.setData(singleGlobalNavWidgetModelCardImage, this)
    }

    override fun onClickItem(item: GlobalNavWidgetModel.Item) {
        Snackbar.make(constraintLayoutGlobalNavWidget, item.clickItemApplink, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel) {
        Snackbar.make(constraintLayoutGlobalNavWidget, globalNavWidgetModel.clickSeeAllApplink, Snackbar.LENGTH_SHORT).show()
    }
}
