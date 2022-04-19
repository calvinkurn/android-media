import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModelTest
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModelTest
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenterTest
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightPresenterTest
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        TopAdsCreditHistoryViewModelTest::class,
        TopAdsAutoTopUpViewModelTest::class,
        TopAdsDashboardPresenterTest::class,
        TopAdsInsightPresenterTest::class,
        GroupDetailViewModelTest::class
)
class TopAdsDashSuite