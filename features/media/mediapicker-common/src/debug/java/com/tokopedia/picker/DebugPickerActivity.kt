package com.tokopedia.picker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.databinding.ActivityPickerDebugBinding
import com.tokopedia.picker.common.EXTRA_PICKER_PARAM
import com.tokopedia.picker.common.RESULT_PICKER
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.toUiModel
import com.tokopedia.picker.widget.drawerselector.DebugDrawerActionType
import com.tokopedia.picker.widget.drawerselector.DebugDrawerSelectionWidget
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File

class DebugPickerActivity : AppCompatActivity(), DebugDrawerSelectionWidget.Listener {

    private val binding: ActivityPickerDebugBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_debug)
        initDefaultApplink()
        initConfig()

        binding?.btnAction?.setOnClickListener {
            val appLink = binding?.edtApplink?.editText?.text?: ""
            val intent = RouteManager.getIntent(applicationContext, appLink.toString()).apply {
                val json = binding?.edtConfig?.text?: ""
                val fromJson = Gson().fromJson(json.toString(), PickerParam::class.java)
                putExtra(EXTRA_PICKER_PARAM, fromJson)
            }

            startActivityForResult(intent, REQUEST_PICKER_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            val elements = intent?.getStringArrayListExtra(RESULT_PICKER)?: return
            val uiModels = elements.map { File(it).toUiModel() }
            binding?.drawerSelector?.addAllData(uiModels)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.drawerSelector?.setListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding?.drawerSelector?.removeListener()
    }

    override fun onDataSetChanged(action: DebugDrawerActionType) {

    }

    override fun onItemClicked(media: MediaUiModel) {

    }

    private fun initDefaultApplink() {
        binding?.edtApplink?.editText?.setText(
            ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
        )
    }

    private fun initConfig() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val toJson = gson.toJson(PickerParam())
        binding?.edtConfig?.setText(toJson)
    }

    companion object {
        private const val REQUEST_PICKER_CODE = 123
    }

}