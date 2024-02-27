package com.tkpd.macrobenchmark.util

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until

object MacroDevOps {
    const val LEWATI_BTN = "Lewati"
    fun setupEnvironment(intent: Intent) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.targetContext.startActivity(intent)
        Thread.sleep(5000)
    }

    fun skipOnboarding() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.targetContext.startActivity(
                MacroIntent.Home.getHomeIntent().apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            val device = UiDevice.getInstance(instrumentation)

            skipOnboardingPage()

            device.pressBack()

            Thread.sleep(300L)
            killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
        } catch (e: Exception) {
            // no-op
        }
    }

    fun skipOnboardingSellerApp() {
        try {
            val packageName = MacroIntent.TKPD_PACKAGE_SELLER_APP
            val instrumentation = InstrumentationRegistry.getInstrumentation()

            val intent = Intent("android.intent.action.VIEW")
            intent.data = Uri.parse("sellerapp://welcome")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage(packageName)

            instrumentation.targetContext.startActivity(intent)
            val device = UiDevice.getInstance(instrumentation)

            skipOnboardingPageSellerApp()

            Thread.sleep(2000L)
            killProcess(device, packageName)
        } catch (e: Exception) {
            // no-op
        }
    }

    fun skipOnboardingPage() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val device = UiDevice.getInstance(instrumentation)

            waitUntilObject(device, By.text(LEWATI_BTN))
            val btn = device.findObject(By.text(LEWATI_BTN))
            btn.click()
            // in case there is dialog/bottomsheet
            device.pressBack()
        } catch (e: Exception) {
            // no-op
        }
    }

    private fun skipOnboardingPageSellerApp() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val device = UiDevice.getInstance(instrumentation)

            waitUntilObject(device, By.text(LEWATI_BTN))
            val btn = device.findObject(By.text(LEWATI_BTN))
            btn.click()

        } catch (e: Exception) {
            // no-op
        }
    }

    fun setupLoginFlow(
        packageName: String = "com.tokopedia.tkpd",
        email: String = "pbs-hidayatullah+prod7@tokopedia.com",
        password: String = "Prod1234"
    ) {
        setupEnvironment(
            MacroIntent.Session.getSessionMacroSetupIntent(packageName)
        )

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        val result = device.findObject(UiSelector().text("Selanjutnya"))
        Log.d("DebugMacro", result.exists().toString())

        if (!result.exists()) {
            // possibly goto login flow
            setupGotoLogin(device, packageName, email, password)
        } else {
            // possibly tokopedia login flow
            setupTokopediaLogin(device, packageName, email, password)
        }
    }

    fun setupGotoLogin(
        device: UiDevice,
        packageName: String,
        email: String = "pbs-hidayatullah+prod7@tokopedia.com",
        password: String = "Prod1234"
    ) {
        val findObjectEmail = By.res(packageName, "text_field_input")
        waitUntilObject(device, findObjectEmail)
        val emailField = device.findObject(findObjectEmail)
        emailField.setText(email)

        val lanjutButton = device.findObject(UiSelector().text("Lanjut"))
        lanjutButton.click()

        Thread.sleep(2000)

        val findObjectPassword = By.res(packageName, "text_field_input")
        waitUntilObject(device, findObjectEmail)
        val textFieldPassword = device.findObject(findObjectPassword)
        textFieldPassword.setText(password)

        val lanjutkanButton = device.findObject(UiSelector().text("Lanjutkan"))
        lanjutkanButton.click()

        Thread.sleep(5000)
        device.pressHome()
    }

    fun setupTokopediaLogin(
        device: UiDevice,
        packageName: String,
        email: String = "pbs-hidayatullah+prod7@tokopedia.com",
        password: String = "Prod1234"
    ) {
        val findObjectEmail = By.res(packageName, "text_field_input")
        waitUntilObject(device, findObjectEmail)
        val emailField = device.findObject(findObjectEmail)
        emailField.setText(email)

        val selanjutnyaButton = device.findObject(UiSelector().text("Selanjutnya"))
        selanjutnyaButton.click()

        Thread.sleep(2000)
        val findObjectInputView = By.res(packageName, "login_input_view")
        waitUntilObject(device, findObjectInputView)

        val loginInputView = device.findObject(findObjectInputView)
        val wrapper = loginInputView.findObject(By.res(packageName, "wrapper_password"))
        val passwordField = wrapper.findObject(By.res(packageName, "text_field_input"))

        passwordField.setText(password)

        val masukButton = loginInputView.findObject(By.res(packageName, "register_btn"))
        masukButton.click()

        Thread.sleep(5000)
        device.pressHome()
    }

    fun openTargetThenKill(intent: Intent) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.targetContext.startActivity(
            intent.apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
        Thread.sleep(2000L)
        val device = UiDevice.getInstance(instrumentation)
        device.pressBack()
        Thread.sleep(300L)
        killProcess(device, MacroIntent.TKPD_PACKAGE_NAME)
    }

    private fun waitUntilObject(device: UiDevice, findObjectEmail: BySelector?) {
        device.wait(
            Until.hasObject(findObjectEmail),
            5000L
        )
    }

    fun killProcess(device: UiDevice, packageName: String) {
        Log.d("TkpdMacroBenchmark", "Killing process $packageName")
        device.executeShellCommand("am force-stop $packageName")
    }
}
