package com.tokopedia.globalnavwidgetsamples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel
import com.tokopedia.globalnavwidgetsamples.databinding.ActivityGlobalNavWidgetBinding

class GlobalNavWidgetSamplesActivity : AppCompatActivity(), GlobalNavWidgetListener {
    private lateinit var binding: ActivityGlobalNavWidgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalNavWidgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.globalNavWidgetPillWithNoInfo.setData(globalNavWidgetPillModelItemNoInfo, this)
        binding.globalNavWidgetPillWithInfo.setData(globalNavWidgetPillModelItemWithInfo, this)
        binding.globalNavWidgetPillWithoutSeeAllApplink.setData(globalNavWidgetPillModelWithoutSeeAllApplink, this)
        binding.globalNavWidgetWithoutBackground.setData(globalNavWidgetWithoutBackgroundInModel, this)
        binding.globalNavWidgetCard.setData(globalNavWidgetModelCard, this)
        binding.globalNavWidgetCard2.setData(globalNavWidgetModelCard2, this)
        binding.singleGlobalNavWidgetPill.setData(singleGlobalNavWidgetModelPill, this)
        binding.singleGlobalNavWidgetCardIconWithNoSubtitleInfo.setData(singleGlobalNavWidgetModelCardIconWithNoSubtitleInfo, this)
        binding.singleGlobalNavWidgetCardIcon.setData(singleGlobalNavWidgetModelCardIcon, this)
        binding.singleGlobalNavWidgetCardImage.setData(singleGlobalNavWidgetModelCardImage, this)
    }

    override fun onClickItem(item: GlobalNavWidgetModel.Item) {
        Snackbar.make(binding.constraintLayoutGlobalNavWidget, item.clickItemApplink, Snackbar.LENGTH_SHORT).show()
    }

    override fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel) {
        Snackbar.make(binding.constraintLayoutGlobalNavWidget, globalNavWidgetModel.clickSeeAllApplink, Snackbar.LENGTH_SHORT).show()
    }
}
