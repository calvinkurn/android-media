package com.tokopedia.discovery.catalog.presenter;

import android.app.Activity;

import com.tokopedia.discovery.catalog.model.CatalogImage;
import com.tokopedia.core.model.share.ShareData;

import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public interface ICatalogDetailPresenter {
    /**
     * segala macam logic business untuk ambil dan olah data detail catalog
     *
     * @param activity  buat generate Auth param ws4
     * @param catalogId param catalog_id ke endpoint
     */
    void processGetCatalogDetailData(Activity activity, String catalogId);

    /**
     * proses share catalog
     *
     * @param activity  digunakan untuk get resource string
     * @param shareData data yang ingin di lempar ke share activity
     */
    void processShareCatalog(Activity activity, ShareData shareData);

    /**
     * proses tampilkan gambar fullscreen
     *
     * @param activity         sebagai context untuk membuat intent
     * @param currentItem      index gambar
     * @param catalogImageList untuk membuat image list url
     */
    void processShowCatalogImageFullScreen(Activity activity, int currentItem,
                                           List<CatalogImage> catalogImageList);

    /**
     * Unsubscribe observable when view is destroyed
     */
    void unsubscribeObservable();
}
