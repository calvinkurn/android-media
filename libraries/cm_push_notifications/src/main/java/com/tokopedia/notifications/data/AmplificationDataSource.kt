package com.tokopedia.notifications.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.PushController
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.IrisAnalyticsEvents.INAPP_DELIVERED
import com.tokopedia.notifications.common.IrisAnalyticsEvents.sendAmplificationInAppEvent
import com.tokopedia.notifications.data.model.Amplification
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.AmplificationCMInApp
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor
import com.tokopedia.notifications.utils.NextFetchCacheManager
import com.tokopedia.user.session.UserSession
import java.util.concurrent.TimeUnit
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw

object AmplificationDataSource {

    private val useCase by lazy {
        GraphqlUseCase<AmplificationNotifier>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @JvmStatic fun invoke(application: Application) {
        val cacheManager = NextFetchCacheManager(application)
        val currentTime = System.currentTimeMillis()
        val userSession = UserSession(application)

        /*
        * preventing amplification data request
        * if user haven't login yet
        * */
        if (!userSession.isLoggedIn) return

        /*
        * preventing multiple fetching of amplification data
        * check based-on `next_fetch` from payload
        * */
        if (currentTime <= cacheManager.getNextFetch()) {
            return
        }

        val query = loadRaw(application.resources, R.raw.query_notification_amplification)
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        RepositoryManager.initRepository(application)
        Toast.makeText(application, "Amplification API hit", Toast.LENGTH_LONG).show()

        amplificationUseCase.execute {

            Toast.makeText(application, "Amplification API response received", Toast.LENGTH_LONG).show()

            val webHook = it.webhookAttributionNotifier
            pushData(application, webHook)
            inAppData(application, webHook)

            // save `next_fetch` time data
            val nextFetchTime = webHook.nextFetch
            cacheManager.saveNextFetch(nextFetch(nextFetchTime))
        }
    }

    private fun pushData(application: Application, amplification: Amplification) {
        if (amplification.pushData.isNotEmpty()) {
            Toast.makeText(application, "Push data present - size: " + amplification.pushData.size, Toast.LENGTH_LONG).show()
            amplification.pushData.forEach {
//        val it = "{\"id\":-1112,\"icon\":\"\",\"sound\":\"\",\"notificationId\":-1112,\"source\":\"toko-cm\",\"tribe\":\"toko-cm\",\"notificationType\":\"General\",\"notificationProductType\":\"\",\"priorityPreOreo\":0,\"subText\":\"\",\"startTime\":\"1620181990000\",\"endTime\":\"1621468390000\",\"userId\":\"76681493\",\"shopId\":\"\",\"transId\":\"LTM0MSM3NjY4MTQ5MyMxNjIwMTgxOTkwNzg4NjQyMjU5\$EPZ-VJMBQZYNWFLRWBDIH4:APA91BFFK_PLOJS3W5FSLGBDECXFQWUVZJISKLHHUKBOM_AGG6XBB8BDJ5M4B9L_JKSUBGDBXAXAIYZ4I6UZ_NBAV9EUG-HPN3HQJK7TXONIRLQ93EAJ0R0NWUVXDTDJKW3PHB9NXHEV\",\"userTransId\":\"LTM0MSM3NjY4MTQ5MyMxNjIwMTgxOTkwNzg4NjQyMjU5\",\"notifcenterBlastId\":\"\",\"channel\":\"\",\"isTest\":false,\"title\":\"Halo Toppers, selamat pagi nih! Ampli #1\",\"desc\":\"\",\"message\":\"Ayo cek Tokopedia yuk, banyak barang bagus yang bisa kamu beli\",\"appLink\":\"tokopedia://home\",\"ceid\":0,\"eeid\":0,\"webhook_params\":\"\",\"campaignUserToken\":\"LTM0MSM3NjY4MTQ5MyMxNjIwMTgxOTkwNzg4NjQyMjU5\$EPZ-VJMBQZYNWFLRWBDIH4:APA91BFFK_PLOJS3W5FSLGBDECXFQWUVZJISKLHHUKBOM_AGG6XBB8BDJ5M4B9L_JKSUBGDBXAXAIYZ4I6UZ_NBAV9EUG-HPN3HQJK7TXONIRLQ93EAJ0R0NWUVXDTDJKW3PHB9NXHEV\",\"campaignId\":-1112,\"parentId\":-1112,\"is_common_service\":true,\"is_last_batch\":true,\"event_time\":{\"request\":\"2021-05-05T09:33:10+07:00\",\"sent\":\"2021-05-05T09:33:11.051375509+07:00\"},\"mainappPriority\":1,\"sellerappPriority\":1,\"isAdvanceTarget\":true}"
                PushController(application).handleNotificationAmplification(it)
            }
        } else
            Toast.makeText(application, "Push data empty", Toast.LENGTH_LONG).show()
    }

    private fun inAppData(application: Application, amplification: Amplification) {
        if (amplification.inAppData.isNotEmpty()) {
            Toast.makeText(application, "Inapp data present - size: " + amplification.inAppData.size, Toast.LENGTH_LONG).show()
            amplification.inAppData.forEach {
                try {
//                    val it = "{\"appLink\":\"tokopedia://home\",\"campaignId\":-1111,\"campaignUserToken\":\"LTgxNSM3NjY4MTQ5Mw==\$EPZ-VJMBQZYNWFLRWBDIH4:APA91BFFK_PLOJS3W5FSLGBDECXFQWUVZJISKLHHUKBOM_AGG6XBB8BDJ5M4B9L_JKSUBGDBXAXAIYZ4I6UZ_NBAV9EUG-HPN3HQJK7TXONIRLQ93EAJ0R0NWUVXDTDJKW3PHB9NXHEV\",\"d\":true,\"event_time\":{\"request\":\"2021-05-05T09:25:11.375956169+07:00\",\"sent\":\"0001-01-01T00:00:00Z\"},\"freq\":1,\"isTest\":false,\"notificationId\":-1111,\"notificationType\":\"interstitial\",\"parentId\":-1111,\"perstOn\":false,\"s\":\"com.tokopedia.navigation.presentation.activity.MainParentActivity\",\"source\":\"toko-cm-inapp\",\"ss\":\"\",\"ui\":{\"appLink\":\"tokopedia://discovery/pasar-sabtu-minggu\",\"bg\":{\"clr\":\"#00000000\",\"img\":\"\",\"rd\":5,\"sc\":\"#00000000\",\"sw\":1},\"btnOri\":\"horizontal\",\"img\":\"https://ecs7.tokopedia.net/img/VqbcmM/2020/6/8/3d4da779-a9c6-4fde-9265-42fdc55e9cca.jpg\",\"inAppButtons\":[{\"appLink\":\"tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels\",\"bgc\":\"#f17808\",\"clr\":\"#ffffff\",\"id\":1,\"sz\":\"14\",\"text\":\"Cek Sekarang #1\",\"unifySize\":\"large\",\"unifyType\":\"main\",\"unifyVariant\":\"filled\"}],\"msg\":{\"clr\":\"#ffffff\",\"sz\":\"0\",\"txt\":\"\"},\"ttl\":{\"clr\":\"#ffffff\",\"sz\":\"0\",\"txt\":\"\"}}}"
                    val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                    val amplificationCMInApp: AmplificationCMInApp = gson.fromJson(it, AmplificationCMInApp::class.java)

                    val cmInApp = CmInAppBundleConvertor.getCmInApp(amplificationCMInApp)
                    // flag if this data comes from amplification fetch API
                    amplificationCMInApp.isAmplification = true

                    // storage to local storage
                    RepositoryManager
                            .getInstance()
                            .storageProvider
                            .putDataToStore(cmInApp)
                            .subscribe()

                    // send amplification tracker
                    sendAmplificationInAppEvent(application, INAPP_DELIVERED, cmInApp)
                } catch (e: Exception) {
                    println(e.message)
                    ServerLogger.log(Priority.P2, "CM_VALIDATION",
                            mapOf("type" to "exception",
                                    "err" to Log.getStackTraceString(e)
                                            .take(CMConstant.TimberTags.MAX_LIMIT),
                                    "data" to ""))
                }
            }
        } else
            Toast.makeText(application, "Popup data empty", Toast.LENGTH_LONG).show()
    }

    private fun nextFetch(time: Long): Long {
        val currentTime = System.currentTimeMillis()
        val secondToMilis = TimeUnit.SECONDS.toMillis(time)
        return currentTime + secondToMilis
    }

}