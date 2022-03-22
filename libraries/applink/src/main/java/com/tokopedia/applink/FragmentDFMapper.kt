package com.tokopedia.applink

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.tokopedia.applink.FragmentConst.DF_INSTALLER_FRAGMENT_CLASS_PATH
import com.tokopedia.config.GlobalConfig
import tokopedia.applink.R

/**
 * Fragment Dynamic Feature Mapper
 *
 * map fragment class path to df module
 */
object FragmentDFMapper {

    private const val BUNDLE_KEY_MODULE_ID = "MODULE_ID"
    private const val BUNDLE_KEY_MODULE_NAME = "MODULE_NAME"
    private const val BUNDLE_ARGUMENTS_KEY_EXTRAS = "BUNDLE_ARGUMENTS_EXTRAS"
    private const val BUNDLE_KEY_CLASS_NAME = "CLASS_NAME"
    private var manager: SplitInstallManager? = null
    val fragmentDfModuleMapperCustomerApp: List<FragmentDFPattern> by lazy {
        mutableListOf<FragmentDFPattern>().apply {
            /**
             * You can add the map of the fragment of a specific module here
             * For example
             * add(FragmentDFPattern(
                    logic to match the given className E.g.: { it == FRAGMENT_CLASS_PATH },
                    MODULE_ID,
                    MODULE_NAME_RESOURCE_ID
               ))
             */
            add(FragmentDFPattern(
                    {it == FragmentConst.SHOP_SHOWCASE_TAB_FRAGMENT_CLASS_PATH},
                    DeeplinkDFMapper.DF_BASE,
                    R.string.shop_showcase_tab
            ))
        }
    }

    val fragmentDfModuleMapperSellerApp: List<FragmentDFPattern> by lazy {
        mutableListOf<FragmentDFPattern>().apply {
            /**
             * You can add the map of the fragment of a specific module here
             * For example
             * add(FragmentDFPattern(
            logic to match the given className E.g.: { it == FRAGMENT_CLASS_PATH },
            MODULE_ID,
            MODULE_NAME_RESOURCE_ID
            ))
             */
            add(FragmentDFPattern(
                {it == FragmentConst.SHOP_SHOWCASE_TAB_FRAGMENT_CLASS_PATH},
                DeeplinkDFMapper.DF_BASE_SELLER_APP,
                R.string.shop_showcase_tab
            ))
        }
    }

    private fun getSplitManager(context: Context): SplitInstallManager? {
        if (manager === null) {
            manager = SplitInstallManagerFactory.create(context.applicationContext)
        }
        return manager
    }

    @JvmStatic
    fun checkIfFragmentIsInstalled(context: Context, className: String): Boolean {
        return if(isClassExist(className))
            true
        else{
            val matchedFragmentDFPattern = getMatchedFragmentDFPattern(className)
            val moduleId = matchedFragmentDFPattern?.moduleId.orEmpty()
            getSplitManager(context)?.installedModules?.contains(moduleId) ?: false
        }
    }

    private fun isClassExist(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    @JvmStatic
    fun getMatchedFragmentDFPattern(
            className: String,
    ): FragmentDFPattern? {
        val fragmentDfModuleMapper = if (GlobalConfig.isSellerApp()) {
            fragmentDfModuleMapperSellerApp
        } else {
            fragmentDfModuleMapperCustomerApp
        }
        return fragmentDfModuleMapper.firstOrNull {
            it.logic(className)
        }
    }

    @JvmStatic
    fun getFragmentDFDownloader(activity: AppCompatActivity, className: String, extras: Bundle?): Fragment? {
        return getSplitManager(activity)?.let {
            val matchedFragmentDFPattern = getMatchedFragmentDFPattern(className)
            matchedFragmentDFPattern?.let { fragmentDfMapper ->
                val moduleId = fragmentDfMapper.moduleId
                val moduleName = activity.getString(fragmentDfMapper.moduleNameResourceId)
                val bundle = Bundle().apply {
                    putString(BUNDLE_KEY_MODULE_ID, moduleId)
                    putString(BUNDLE_KEY_MODULE_NAME, moduleName)
                    putString(BUNDLE_KEY_CLASS_NAME, className)
                    putBundle(BUNDLE_ARGUMENTS_KEY_EXTRAS, extras)
                }
                RouteManager.instantiateFragment(activity, DF_INSTALLER_FRAGMENT_CLASS_PATH, bundle)
            }
        }
    }

}

/**
 * Class to hold dynamic feature fragment pattern, used for mapping
 */
class FragmentDFPattern(
        val logic: ((className: String) -> Boolean),
        val moduleId: String,
        val moduleNameResourceId: Int
)