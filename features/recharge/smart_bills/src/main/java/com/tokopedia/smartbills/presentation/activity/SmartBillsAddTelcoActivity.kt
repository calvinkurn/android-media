package com.tokopedia.smartbills.presentation.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsAddTelcoFragment

/**
 * applink
 * tokopedia://digital/form?category_id=9&menu_id=3&template=telcopost&is_add_sbm=true
 * or
 * tokopedia://digital/form?category_id=1&menu_id=2&template=telcopre&is_add_sbm=true
 */

class SmartBillsAddTelcoActivity: BaseSimpleActivity(), HasComponent<SmartBillsComponent> {

    private var templateTelco : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.dataString
        templateTelco = parseQuery(TEMPLATE, uri, intent.extras)
    }

    override fun getComponent(): SmartBillsComponent {
        return DaggerSmartBillsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getNewFragment(): Fragment {
        return SmartBillsAddTelcoFragment.newInstance(templateTelco)
    }

    private fun parseQuery(textToFind: String, uriString: String?,
                           bundle: Bundle?): String{
        var text: String? = ""
        var uri: Uri = Uri.EMPTY
        if(bundle != null){
            if(bundle.getString(textToFind) != null){
                text= bundle.getString(textToFind, "")
                return text
            }
        }

        uri = Uri.parse(uriString)
        text = uri.getQueryParameter(textToFind)

        return text ?: ""
    }

    companion object{
        private const val TEMPLATE = "template"
    }
}