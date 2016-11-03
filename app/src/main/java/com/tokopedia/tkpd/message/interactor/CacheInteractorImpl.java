package com.tokopedia.tkpd.message.interactor;

import com.tokopedia.tkpd.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.var.TkpdState;

/**
 * Created by Angga.Prasetiyo on 26/01/2016.
 */
public class CacheInteractorImpl implements CacheInteractor {
    private static final String TAG = CacheInteractorImpl.class.getSimpleName();
    private GlobalCacheManager cacheManager;

    public CacheInteractorImpl() {
        this.cacheManager = new GlobalCacheManager();
    }

    public void deleteCache(){
        cacheManager.delete(TkpdState.CacheKeys.INBOX_MESSAGE);
        cacheManager.delete(TkpdState.CacheKeys.INBOX_ARCHIVE);
        cacheManager.delete(TkpdState.CacheKeys.INBOX_SENT);
        cacheManager.delete(TkpdState.CacheKeys.INBOX_TRASH);
    }
}
