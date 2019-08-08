package com.tokopedia.tkpd

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import kotlinx.android.synthetic.main.activity_empty.*

class EmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        button_show.setOnClickListener {
            show()
        }
    }

    private fun show() {
        val dialog = DistrictRecommendationBottomSheetFragment.newInstance()
        dialog.setActionListener(object: DistrictRecommendationBottomSheetFragment.ActionListener {
            override fun onGetDistrict(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel) {
                tv_result.text = districtRecommendationItemUiModel.toString()
            }
        })
        dialog.show(supportFragmentManager, "")
    }
}
