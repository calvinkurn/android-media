package com.tokopedia.core;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.payment.model.responsecartstep2.SystemBank;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mockito;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by ricoharisin on 3/1/17.
 */

public class OkHttpTest extends TestCase {

    @Test
    public void testBuildClientAuth() {
        System.out.println("testBuildClientAuth");
        OkHttpClient client = OkHttpFactory.create().buildClientDefaultAuth();
        System.out.println("Interceptor: "+client.interceptors().toString());
        assertEquals(client.readTimeoutMillis(), 45000);
        assertEquals(client.writeTimeoutMillis(), 45000);
        assertEquals(client.connectTimeoutMillis(), 45000);
        assertTrue(client.interceptors().toString().contains("TkpdAuthInterceptor"));
        for (Interceptor interceptor : client.interceptors()) {
            if (interceptor instanceof TkpdBaseInterceptor) {
                System.out.println("Interceptor found");
                assertEquals(((TkpdBaseInterceptor) interceptor).getMaxRetryAttempt(), 3);
            }
        }
    }

    @Test
    public void testBuildClientNoAuth() {
        System.out.println("testBuildClientNoAuth");
        OkHttpClient client = OkHttpFactory.create().buildClientNoAuth();
        System.out.println("Interceptor: "+client.interceptors().toString());
        assertEquals(client.readTimeoutMillis(), 45000);
        assertEquals(client.writeTimeoutMillis(), 45000);
        assertEquals(client.connectTimeoutMillis(), 45000);
        assertTrue(client.interceptors().toString().contains("TkpdBaseInterceptor"));
        for (Interceptor interceptor : client.interceptors()) {
            if (interceptor instanceof TkpdBaseInterceptor) {
                System.out.println("Interceptor found");
                assertEquals(((TkpdBaseInterceptor) interceptor).getMaxRetryAttempt(), 3);
            }
        }
    }

    @Test
    public void testBuildClientAuthWithRetryPolicy() {
        System.out.println("testBuildClientAuthWithRetryPolicy");
        OkHttpRetryPolicy policy = new OkHttpRetryPolicy(10, 20, 30, 100);
        OkHttpClient client = OkHttpFactory.create().addOkHttpRetryPolicy(policy).buildClientDefaultAuth();
        System.out.println("Interceptor: "+client.interceptors().toString());
        assertEquals(client.readTimeoutMillis(), policy.readTimeout*1000);
        assertEquals(client.writeTimeoutMillis(), policy.writeTimeout*1000);
        assertEquals(client.connectTimeoutMillis(), policy.connectTimeout*1000);
        assertTrue(client.interceptors().toString().contains("TkpdAuthInterceptor"));
        for (Interceptor interceptor : client.interceptors()) {
            if (interceptor instanceof TkpdBaseInterceptor) {
                System.out.println("Interceptor found");
                assertEquals(((TkpdBaseInterceptor) interceptor).getMaxRetryAttempt(), policy.maxRetryAttempt);
            }
        }
    }

    @Test
    public void testBuildClientNoAuthWithRetryPolicy() {
        System.out.println("testBuildClientNoAuthWithRetryPolicy");
        OkHttpRetryPolicy policy = new OkHttpRetryPolicy(10, 20, 30, 100);
        OkHttpClient client = OkHttpFactory.create().addOkHttpRetryPolicy(policy).buildClientNoAuth();
        System.out.println("Interceptor: "+client.interceptors().toString());
        assertEquals(client.readTimeoutMillis(), policy.readTimeout*1000);
        assertEquals(client.writeTimeoutMillis(), policy.writeTimeout*1000);
        assertEquals(client.connectTimeoutMillis(), policy.connectTimeout*1000);
        assertTrue(client.interceptors().toString().contains("TkpdBaseInterceptor"));
        for (Interceptor interceptor : client.interceptors()) {
            if (interceptor instanceof TkpdBaseInterceptor) {
                System.out.println("Interceptor found");
                assertEquals(((TkpdBaseInterceptor) interceptor).getMaxRetryAttempt(), policy.maxRetryAttempt);
            }
        }
    }

}