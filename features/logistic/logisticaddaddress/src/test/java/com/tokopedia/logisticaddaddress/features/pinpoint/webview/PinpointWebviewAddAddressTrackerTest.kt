package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.repository.KeroRepository
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.analytics.AddAddressPinpointTracker
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
class PinpointWebviewAddAddressTrackerTest {
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
        every { liveDataObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `WHEN user click cari lokasi field in add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11045", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickFieldCariLokasi
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click dropdown suggestion add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11046", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickDropdownSuggestion
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click lokasi saat ini in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11047", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickGunakanLokasiSaatIniSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click allow location in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11050", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickAllowLocationSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click dont allow location in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11051", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickDontAllowLocationSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click aktifkan layanan lokasi in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11052", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickAktifkanLayananLokasiSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click x on block gps in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11053", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickXOnBlockGpsSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click back arrow in search page add address THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11055", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickBackArrowSearch
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user view bottomsheet alamat tidak terdeteksi THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11056", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ImpressBottomSheetAlamatTidakTerdeteksi
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click isi alamat manual on undetected location THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11057", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickIsiAlamatManualUndetectedLocation
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user view bottomsheet out of indo THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11058", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ImpressBottomSheetOutOfIndo
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click isi alamat out of indo THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11059", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickIsiAlamatOutOfIndo
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click cari ulang alamat THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11062", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickCariUlangAlamat
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click gunakan lokasi saat ini in pinpoint page THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11063", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickGunakanLokasiSaatIniPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click allow lokasi in pinpoint page THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11064", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickAllowLocationPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click dont allow lokasi in pinpoint page THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11065", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickDontAllowLocationPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click aktifkan layanan lokasi in pinpoint page THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11066", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickAktifkanLayananLokasiPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click x on block gps in pinpoint page THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11067", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickXOnBlockGpsPinpoint
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click icon question THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11068", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickIconQuestion
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click pilih lokasi in positive flow THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11069", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiPositive
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user click pilih lokasi in negative flow THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_NEGATIVE")

        viewModel.sendTracker("11083", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ClickPilihLokasiNegative
                    )
                }
            )
        }
    }

    @Test
    fun `WHEN user view toaster pinpoint tidak sesuai THEN should hit analytic`() {
        viewModel.setSource("ADD_ADDRESS_POSITIVE")

        viewModel.sendTracker("11084", null)

        verify {
            liveDataObserver.onChanged(
                match { state ->
                    state == PinpointWebviewState.SendTracker.AddAddress(
                        AddAddressPinpointTracker.ViewToasterPinpointTidakSesuai
                    )
                }
            )
        }
    }
}
