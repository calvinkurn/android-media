package com.tokopedia.discovery.catalog.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core2.R;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.catalog.interactor.CatalogDataInteractor;
import com.tokopedia.discovery.catalog.interactor.ICataloDataInteractor;
import com.tokopedia.discovery.catalog.listener.IDetailCatalogView;
import com.tokopedia.discovery.catalog.model.CatalogDetailData;
import com.tokopedia.discovery.catalog.model.CatalogImage;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogDetailPresenter implements ICatalogDetailPresenter {
    private final IDetailCatalogView catalogView;
    private final ICataloDataInteractor dataInteractor;

    public CatalogDetailPresenter(IDetailCatalogView iDetailCatalogView) {
        this.catalogView = iDetailCatalogView;
        this.dataInteractor = new CatalogDataInteractor();
    }


    @Override
    public void processGetCatalogDetailData(final Activity activity,final String catalogId) {
        catalogView.showMainProcessLoading();
        catalogView.cleanAllContent();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("catalog_id", catalogId);
        param.put("no_shop", "1");
        dataInteractor.getDetailCatalogData(AuthUtil.generateParamsNetwork(activity, param),
                new Subscriber<CatalogDetailData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        catalogView.hideMainProcessLoading();
                        if (e instanceof UnknownHostException) {
                            catalogView.renderErrorNoConnectionGetDetailCatalogData(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            catalogView.renderErrorTimeoutConnectionGetDetailCatalogData(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                            );
                        } else {
                            catalogView.renderErrorGetDetailCatalogData(
                                    ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                            );
                        }
                    }

                    @Override
                    public void onNext(CatalogDetailData catalogDetailData) {
                        catalogView.hideMainProcessLoading();
                        catalogView.renderCatalogDescription(
                                MethodChecker.fromHtml(catalogDetailData.getCatalogInfo()
                                        .getCatalogDescription()).toString()
                        );
                        catalogView.renderCatalogImage(
                                catalogDetailData.getCatalogInfo().getCatalogImageList()
                        );
                        catalogView.renderCatalogInfo(
                                catalogDetailData.getCatalogInfo()
                        );
                        if (!catalogDetailData.getCatalogReview().getReviewFrom()
                                .equalsIgnoreCase("0"))
                            catalogView.renderCatalogReview(
                                    catalogDetailData.getCatalogReview()
                            );
                        catalogView.renderCatalogSpec(
                                catalogDetailData.getCatalogSpecList()
                        );

                        catalogView.renderCatalogShareData(
                                generateCatalogShareData(activity,
                                        catalogDetailData.getCatalogInfo().getCatalogUrl(), catalogId)
                        );
                        catalogView.renderButtonBuy();
                    }
                });
    }

    private ShareData generateCatalogShareData(Activity activity, String catalogUrl,String catalogId) {
        return ShareData.Builder.aShareData()
                .setId(catalogId)
                .setName(activity.getString(R.string.message_share_catalog))
                .setType(ShareData.CATALOG_TYPE)
                .setTextContent(activity.getString(R.string.share_text_content))
                .setUri(catalogUrl)
                .build();
    }

    @Override
    public void processShareCatalog(Activity activity, ShareData shareData) {
        if (shareData == null) {
            catalogView.showToastMessage("Data katalog belum tersedia");
            return;
        }
        catalogView.navigateToActivity(ShareActivity.createIntent(activity,shareData));
    }

    @Override
    public void processShowCatalogImageFullScreen(Activity activity, int currentItem,
                                                  List<CatalogImage> catalogImageList) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0, catalogImageListSize = catalogImageList.size(); i < catalogImageListSize; i++) {
            CatalogImage catalogImage = catalogImageList.get(i);
            arrayList.add(catalogImage.getImageSrcFull());
        }
        Intent intent = new Intent(activity, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", arrayList);
        bundle.putInt("img_pos", currentItem);
        intent.putExtras(bundle);
        catalogView.navigateToActivity(intent);
    }

    @Override
    public void unsubscribeObservable() {
        dataInteractor.unsubscribe();
    }
}
