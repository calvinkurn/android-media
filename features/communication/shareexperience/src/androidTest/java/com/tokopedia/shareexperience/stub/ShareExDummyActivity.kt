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
            openShareBottomSheet(ShareExBottomSheetArg(
                identifier = "",
                pageTypeEnum = ShareExPageTypeEnum.OTHERS,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg()
            ))
        }
    }
}
