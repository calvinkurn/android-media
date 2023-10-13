import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_CHATBOT_MVVM

object RemoteConfigHelper {
    fun isRemoteConfigForMVVM(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(ENABLE_CHATBOT_MVVM, true)
    }
}
