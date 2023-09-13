package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.EditAddressPinpointTracker
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PinpointWebviewEditAddressTrackerTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: KeroRepository = mockk(relaxed = true)
    private val liveDataObserver: Observer<PinpointWebviewState> = mockk(relaxed = true)

    private lateinit var viewModel: PinpointWebviewViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = PinpointWebviewViewModel(repo)
        viewModel.pinpointState.observeForever(liveDataObserver)
        viewModel.setSource("EDIT_ADDRESS")
        every { liveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `WHEN user click field cari lokasi when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29692", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickFieldCariLokasi
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click dropdown suggestion when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29693", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickDropdownSuggestion
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click gunakan lokasi saat ini in search page when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29694", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickGunakanLokasiSaatIniSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click back arrow in search page when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29695", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickBackArrowSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user view bottomsheet alamat tidak terdeteksi when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29690", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ImpressBottomSheetAlamatTidakTerdeteksi
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user view bottomsheet alamat out of indonesia when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29689", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ImpressBottomSheetOutOfIndo
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click cari ulang alamat when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29687", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickCariUlangAlamat
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click gunakan lokasi saat ini in pinpoint page when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29686", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickGunakanLokasiSaatIniPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click pilih lokasi ini when edit address THEN should hit analytic`() {
        viewModel.sendTracker("29688", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.EditAddress(
                        EditAddressPinpointTracker.ClickPilihLokasiIni
                    )
                }
            )
        }
    }
}
