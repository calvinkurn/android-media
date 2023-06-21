package com.tokopedia.scp_rewards_touchpoints.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.view.toaster.ScpRewardsToaster

class TestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_test)
        super.onCreate(savedInstanceState)
        ScpRewardsToaster.build(findViewById<ConstraintLayout>(R.id.test_root), "Ada medali baru buatmu!", "Cek & ambil bonus-nya, yuk~", 40000, actionText = "Cek", clickListener = {
            MedalCelebrationBottomSheet.show(supportFragmentManager, "UNILEVER_CLUB")
        }).show()
    }

}
