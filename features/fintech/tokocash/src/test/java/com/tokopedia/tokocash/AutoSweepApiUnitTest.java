package com.tokopedia.tokocash;

import com.tokopedia.tokocash.autosweepmf.data.repository.AutoSweepRepositoryImpl;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertTrue;

public class AutoSweepApiUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    AutoSweepRepositoryImpl mAutoSweepRepositoryImpl;

    @Test
    public void isNonNull() {
        if(mAutoSweepRepositoryImpl == null){
            assertTrue(false);
        } else {
            assertTrue(true);
        }
    }

    @Test
    public void getData() {
        mAutoSweepRepositoryImpl.getAutoSweepDetail();
//        Mockito.verify()
    }
}
