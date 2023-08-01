package com.tokopedia.media.picker.common.ui.activity

import android.content.Intent
import com.google.android.gms.tagmanager.PreviewActivity
import com.tokopedia.media.picker.common.ui.TestPickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactoryImpl
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.picker.common.EXTRA_INTENT_PREVIEW
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity

class TestPickerActivity : PickerActivity() {

    override fun createFragmentFactory(): PickerFragmentFactory {
        return TestPickerFragmentFactory(
            mFragmentManager = supportFragmentManager,
            classLoader = applicationContext.classLoader
        )
    }

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

    override fun onContinueClicked() {
        startActivityForResult(Intent(this, PreviewActivity::class.java).apply {
            putExtra(EXTRA_INTENT_PREVIEW, ArrayList(medias))
        }, REQUEST_PREVIEW_PAGE)
    }
}
