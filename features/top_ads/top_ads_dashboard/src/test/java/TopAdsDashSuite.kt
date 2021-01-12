import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModelTest
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        TopAdsCreditHistoryViewModelTest::class,
        TopAdsAutoTopUpViewModelTest::class
)
class TopAdsDashSuite