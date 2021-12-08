package com.tokopedia.picker.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.fragment.PickerFragmentFactory
import com.tokopedia.picker.ui.fragment.PickerFragmentFactoryImpl
import com.tokopedia.picker.ui.fragment.PickerNavigator

class PickerActivity : BaseActivity() {

    private var navigator: PickerNavigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        setupNavigator()

        navigator?.start(PickerFragmentType.PICKER)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator?.cleanUp()
    }

    private fun setupNavigator() {
        navigator = PickerNavigator(
            this,
            R.id.container,
            supportFragmentManager,
            createFragmentFactory()
        )
    }

    private fun createFragmentFactory(): PickerFragmentFactory {
        return PickerFragmentFactoryImpl()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PickerActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

}