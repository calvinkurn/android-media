package com.tokopedia.discovery.catalog.listener;

import com.tokopedia.discovery.catalog.model.CatalogImage;
import com.tokopedia.discovery.catalog.model.CatalogInfo;
import com.tokopedia.discovery.catalog.model.CatalogReview;
import com.tokopedia.discovery.catalog.model.CatalogSpec;
import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.linker.model.LinkerData;

import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public interface IDetailCatalogView extends ViewListener {

    /**
     * render catalog info ke UI view
     *
     * @param catalogInfo object catalog info
     */
    void renderCatalogInfo(CatalogInfo catalogInfo);

    /**
     * render catalog review ke UI
     *
     * @param catalogReview object catalog review
     */
    void renderCatalogReview(CatalogReview catalogReview);

    /**
     * render image catalog ke UI
     *
     * @param catalogImageList data list image catalog
     */
    void renderCatalogImage(List<CatalogImage> catalogImageList);

    /**
     * render keterangan / deskripsi katalog
     *
     * @param catalogDesc string catalog desc dong pasti
     */
    void renderCatalogDescription(String catalogDesc);

    /**
     * render spesifikasi katalog
     *
     * @param catalogSpecList object catalog spec
     */
    void renderCatalogSpec(List<CatalogSpec> catalogSpecList);

    /**
     * nampilin tombol beli
     */
    void renderButtonBuy();

    /**
     * render data untuk share
     *
     * @param shareData share data object
     */
    void renderCatalogShareData(LinkerData shareData);

    /**
     * render data jika saat terjadi error
     *
     * @param message message error
     */
    void renderErrorGetDetailCatalogData(String message);

    /**
     * render data ke UI jika tidak ada koneksi internet
     *
     * @param message message error dong pasti
     */
    void renderErrorNoConnectionGetDetailCatalogData(String message);

    /**
     * Oops timeout coy, render jika timeout connection waktu get detail catalog
     *
     * @param message message error
     */
    void renderErrorTimeoutConnectionGetDetailCatalogData(String message);


    /**
     * nampilin loading full screen
     */
    void showMainProcessLoading();

    /**
     * hilangkan loading full screen
     */
    void hideMainProcessLoading();

    /**
     * hide semua holder view
     */
    void cleanAllContent();
}
