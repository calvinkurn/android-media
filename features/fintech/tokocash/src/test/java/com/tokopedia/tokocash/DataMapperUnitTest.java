package com.tokopedia.tokocash;

import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepDetailMapperEntity;
import com.tokopedia.tokocash.autosweepmf.data.mapper.AutoSweepLimitMapperEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepDetailEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepLimitEntity;
import com.tokopedia.tokocash.autosweepmf.data.model.DetailText;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DataMapperUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final int FAKE_INT = 9;
    private static final String FAKE_STRING = "fake_string";
    private static final double FAKE_DOUBLE = 100.56d;
    @InjectMocks
    AutoSweepDetailMapperEntity dataAutoSweepDetailMapperEntity;
    @InjectMocks
    com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepDetailMapper viewAutoSweepDetailMapper;
    @InjectMocks
    AutoSweepLimitMapperEntity dataAutoSweepLimitMapperEntity;
    @InjectMocks
    com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepLimitMapper viewAutoSweepLimitMapper;

    @Test
    public void autoSweepDetailsDataToDomain_isCorrect() throws Exception {
        ResponseAutoSweepDetail responseData = new ResponseAutoSweepDetail();
        AutoSweepDetailEntity data = new AutoSweepDetailEntity();
        data.setAccountStatus(FAKE_INT);
        data.setAmountLimit(FAKE_INT);
        data.setBalance(FAKE_DOUBLE);
        data.setAutoSweepStatus(FAKE_INT);
        DetailText detailText = new DetailText();
        detailText.setTitle(FAKE_STRING);
        detailText.setContent(FAKE_STRING);
        data.setText(detailText);
//        responseData.setData(data);

//        AutoSweepDetailDomain domain
//                = dataAutoSweepDetailMapperEntity.transform(responseData);

//        if (domain.getAccountStatus() == FAKE_INT
//                && domain.getAmountLimit() == FAKE_INT
//                && domain.getBalance() == FAKE_DOUBLE
//                && domain.getTitle().equalsIgnoreCase(FAKE_STRING)
//                && domain.getContent().equalsIgnoreCase(FAKE_STRING)) {
//            assertTrue(true);
//        } else {
//            assertTrue(false);
//        }
    }

    @Test
    public void autoSweepDetailDomainToView_isCorrect() throws Exception {
        AutoSweepDetailDomain data = new AutoSweepDetailDomain();
        data.setAccountStatus(FAKE_INT);
        data.setAmountLimit(FAKE_INT);
        data.setBalance(FAKE_DOUBLE);
        data.setAutoSweepStatus(FAKE_INT);
        DetailText detailText = new DetailText();
        data.setTitle(FAKE_STRING);
        data.setContent(FAKE_STRING);

        com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail domain
                = viewAutoSweepDetailMapper.transform(data);

        if (domain.getAccountStatus() == FAKE_INT
                && domain.getAmountLimit() == FAKE_INT
                && domain.getBalance() == FAKE_DOUBLE
                && domain.getTitle().equalsIgnoreCase(FAKE_STRING)
                && domain.getContent().equalsIgnoreCase(FAKE_STRING)) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void autoSweepLimitDataToDomain_isCorrect() throws Exception {
        ResponseAutoSweepLimit responseData = new ResponseAutoSweepLimit();
        AutoSweepLimitEntity data = new AutoSweepLimitEntity();
        data.setStatus(true);
//        data.setAmountLimit(FAKE_INT);
//        data.setAutoSweep(FAKE_INT);
//        responseData.setData(data);


//        AutoSweepLimitDomain domain
//                = dataAutoSweepLimitMapperEntity.transform(responseData);

//        if (domain.getAutoSweep() == FAKE_INT
//                && domain.getAmountLimit() == FAKE_INT
//                && domain.isStatus()) {
//            assertTrue(true);
//        } else {
//            assertTrue(false);
//        }
    }

    @Test
    public void autoSweepLimitDomainToView_isCorrect() throws Exception {
        AutoSweepLimitDomain domain = new AutoSweepLimitDomain();
        domain.setStatus(true);
//        domain.setAmountLimit(FAKE_INT);
//        domain.setAutoSweep(FAKE_INT);

        com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit  view
                = viewAutoSweepLimitMapper.transform(domain);

//        if (view.getAutoSweep() == FAKE_INT
//                && view.getAmountLimit() == FAKE_INT
//                && view.isStatus()) {
//            assertTrue(true);
//        } else {
//            assertTrue(false);
//        }
    }
}
