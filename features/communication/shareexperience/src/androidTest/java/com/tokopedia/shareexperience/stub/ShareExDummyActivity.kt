package com.tokopedia.shareexperience.stub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExInitializerArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.util.ShareExInitializer

class ShareExDummyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ShareExInitializer(this).run {
            additionalCheck(ShareExInitializerArg())
            openShareBottomSheet(
                ShareExBottomSheetArg.Builder(
                    pageTypeEnum = ShareExPageTypeEnum.OTHERS,
                    defaultUrl = DEFAULT_URL,
                    trackerArg = ShareExTrackerArg(
                        utmCampaign = ""
                    )
                ).build()
            )
        }
    }

    companion object {
        var DEFAULT_URL = ""

        fun reset() {
            DEFAULT_URL = "https://www.tokopedia.com/"
        }
    }
}
