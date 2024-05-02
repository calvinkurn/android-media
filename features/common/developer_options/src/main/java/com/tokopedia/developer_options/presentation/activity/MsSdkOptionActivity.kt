package com.tokopedia.developer_options.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bytedance.mobsec.metasec.ov.MSConfig
import com.bytedance.mobsec.metasec.ov.MSManagerUtils
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.StringIntIntPair
import com.tokopedia.developer_options.StringIntIntPairOrBuilder
import com.tokopedia.developer_options.mssdk.MsSdkOptionConst
import com.tokopedia.developer_options.mssdk.getStringIntPairBlock
import com.tokopedia.developer_options.mssdk.saveStringIntPair
import com.tokopedia.developer_options.mssdk.saveStringIntPairBlocking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MsSdkOptionActivity: BaseActivity() {
    private lateinit var spinner: Spinner

    private lateinit var lastSelected : StringIntIntPair

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalConfig.isAllowDebuggingTools()) {
            setContentView(R.layout.activity_mssdk_dev)

            // get the data
            lastSelected = getStringIntPairBlock(this)

            setupView(lastSelected)
        } else {
            finish()
        }
    }

    fun setupView(lastSelected: StringIntIntPair) {
        spinner = findViewById<Spinner>(R.id.mssdkcollect_mode)

        val envSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.customized_spinner_item, MsSdkOptionConst.COLLECT_MODE)
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = envSpinnerAdapter

        spinner?.setSelection(lastSelected.intValue)

        setSpinnerSelected()
    }

    private fun setSpinnerSelected() {
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                val s = MsSdkOptionConst.COLLECT_MODE[position]

                val collectMode = when(s){
                    "COLLECT_MODE_ML_MINIMIZE" -> {
                        MSConfig.COLLECT_MODE_ML_MINIMIZE
                    }
                    "COLLECT_MODE_FINANCE"-> {
                        MSConfig.COLLECT_MODE_FINANCE
                    }
                    "COLLECT_MODE_ML_TEEN"-> {
                        MSConfig.COLLECT_MODE_ML_TEEN
                    }
                    "COLLECT_MODE_HELO"-> {
                        MSConfig.COLLECT_MODE_HELO
                    }
                    "COLLECT_MODE_RESSO"-> {
                        MSConfig.COLLECT_MODE_RESSO
                    }
                    "COLLECT_MODE_TIKTOKLITE"-> {
                        MSConfig.COLLECT_MODE_TIKTOKLITE
                    }
                    "COLLECT_MODE_TIKTOK"-> {
                        MSConfig.COLLECT_MODE_TIKTOK
                    }
                    "COLLECT_MODE_TIKTOK_BASE"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_BASE
                    }
                    "COLLECT_MODE_MINIMIZE"-> {
                        MSConfig.COLLECT_MODE_MINIMIZE
                    }
                    "COLLECT_MODE_OV_MINIMIZE"-> {
                        MSConfig.COLLECT_MODE_OV_MINIMIZE
                    }
                    "COLLECT_MODE_ML_PGL_AL"-> {
                        MSConfig.COLLECT_MODE_ML_PGL_AL
                    }
                    "COLLECT_MODE_TIKTOK_U13"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_U13
                    }
                    "COLLECT_MODE_TIKTOK_USUNREGISTER"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_USUNREGISTER
                    }
                    "COLLECT_MODE_TIKTOK_GUEST"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_GUEST
                    }
                    "COLLECT_MODE_TIKTOK_INIT"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_INIT
                    }
                    "COLLECT_MODE_TIKTOK_NONUSEA"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_NONUSEA
                    }
                    "COLLECT_MODE_TIKTOK_USEA"-> {
                        MSConfig.COLLECT_MODE_TIKTOK_USEA
                    }
                    else-> {
                        MSConfig.COLLECT_MODE_DEFAULT
                    }
                }

                // 构建config的Builder对象
                val appID = "573733"
                val mgr = MSManagerUtils.get(appID)
                mgr?.let {
                    mgr.setCollectMode(collectMode)
                    mgr.report("mode_change_cnt")

                    val a = StringIntIntPair.
                        newBuilder()
                        .setCntValue(lastSelected.intValue+1)
                        .setStringValue(s)
                        .setIntValue(position)
                        .build()


                    saveStringIntPairBlocking(spinner.context, a)

                    Log.w(TAG, "enter here!!")
                    Toast.makeText(spinner.context, String.format("selected config %s", s), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        val TAG = MsSdkOptionActivity::class.java.name
    }
}
