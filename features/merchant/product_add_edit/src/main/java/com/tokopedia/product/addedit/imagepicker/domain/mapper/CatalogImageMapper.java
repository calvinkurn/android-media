package com.tokopedia.product.addedit.imagepicker.domain.mapper;

import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;
import com.tokopedia.product.addedit.imagepicker.data.model.CatalogImage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageMapper implements Func1<List<CatalogImage>, Observable<List<CatalogModelView>>> {
    @Inject
    public CatalogImageMapper() {
    }

    @Override
    public Observable<List<CatalogModelView>> call(List<CatalogImage> catalogImages) {
        List<CatalogModelView> catalogModelViews = new ArrayList<>();
        for(CatalogImage catalogImage : catalogImages){
            CatalogModelView catalogModelView = new CatalogModelView();
            catalogModelView.setImageUrl(catalogImage.getImageUrl());
            catalogModelViews.add(catalogModelView);
        }
        return Observable.just(catalogModelViews);
    }
}
