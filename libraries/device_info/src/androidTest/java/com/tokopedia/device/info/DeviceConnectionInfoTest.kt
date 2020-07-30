package com.tokopedia.device.info

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by mzennis on 2020-01-17.
 */

@RunWith(AndroidJUnit4::class)
class DeviceConnectionInfoTest {

    @Test
    fun testGetSsid() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val ssid = DeviceConnectionInfo.getSSID(appContext)
        Assert.assertNotNull(ssid)

        println("$TAG_DEVICE_INFO, SSID: $ssid")
    }

    @Test
    fun testIsConnectWifi() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val isConnectWifi = DeviceConnectionInfo.isConnectWifi(appContext)
        Assert.assertNotNull(isConnectWifi)
        Assert.assertSame(true, isConnectWifi)

        println("$TAG_DEVICE_INFO, IsConnectWifi: $isConnectWifi")
    }

    @Test
    fun testIsConnectCellular() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val isConnectCell = DeviceConnectionInfo.isConnectCellular(appContext)
        Assert.assertNotNull(isConnectCell)
        Assert.assertSame(false, isConnectCell)

        println("$TAG_DEVICE_INFO, IsConnectCellular: $isConnectCell")
    }

    @Test
    fun testIsConnectEthernet() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val isConnectEth = DeviceConnectionInfo.isConnectEthernet(appContext)
        Assert.assertNotNull(isConnectEth)
        Assert.assertSame(false, isConnectEth)

        println("$TAG_DEVICE_INFO, IsConnectEthernet: $isConnectEth")
    }

    @Test
    fun testIsInternetAvailable() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val isInternetAvailable = DeviceConnectionInfo.isInternetAvailable(appContext)
        Assert.assertNotNull(isInternetAvailable)
        Assert.assertSame(true, isInternetAvailable)

        println("$TAG_DEVICE_INFO, IsInternetAvailable: $isInternetAvailable")
    }

    @Test
    fun testGetHttpAgent() {
        val httpAgent = DeviceConnectionInfo.getHttpAgent()
        Assert.assertNotNull(httpAgent)

        println("$TAG_DEVICE_INFO, Http Agent: $httpAgent")
    }

    @Test
    fun testGetCarrierName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val carrierName = DeviceConnectionInfo.getCarrierName(appContext)
        Assert.assertNotNull(carrierName)

        println("$TAG_DEVICE_INFO, Carrier Name: $carrierName")
    }

    @Test
    fun testGetIPAddress() {
        val ipAddress = DeviceConnectionInfo.getIPAddress(true)
        Assert.assertNotNull(ipAddress)

        println("$TAG_DEVICE_INFO, IP Address: $ipAddress")
    }
}