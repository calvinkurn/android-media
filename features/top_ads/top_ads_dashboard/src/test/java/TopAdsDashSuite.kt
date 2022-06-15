import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModelTest
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModelTest
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenterTest
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightPresenterTest
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModelTest
import com.tokopedia.topads.dashboard.viewmodel.TopAdsEducationViewModel
import com.tokopedia.topads.dashboard.viewmodel.TopAdsEducationViewModelTest
import com.tokopedia.topads.dashboard.viewmodel.TopAdsInsightViewModelTest
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
        GroupDetailViewModelTest::class,
        TopAdsDashboardViewModelTest::class,
        TopAdsEducationViewModelTest::class,
        TopAdsInsightViewModelTest::class
)
class TopAdsDashSuite