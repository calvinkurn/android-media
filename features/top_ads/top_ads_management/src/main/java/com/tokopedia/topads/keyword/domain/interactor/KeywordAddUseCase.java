package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class KeywordAddUseCase extends UseCase<AddKeywordDomainModel> {
    public static final String GROUP_ID = "grp_id";
    public static final String KEYWORD_TYPE = "key_typ";
    public static final String KEYWORD_LIST = "key_list";
    private static final String SOURCE = "source";
    private TopAdsKeywordRepository topAdsKeywordRepository;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public KeywordAddUseCase(TopAdsKeywordRepository topAdsKeywordRepository, ShopInfoRepository shopInfoRepository) {
        super();
        this.topAdsKeywordRepository = topAdsKeywordRepository;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<AddKeywordDomainModel> createObservable(RequestParams requestParams) {
        final String groupId = requestParams.getString(GROUP_ID, "");
        final int keywordType = requestParams.getInt(KEYWORD_TYPE, KeywordTypeDef.KEYWORD_TYPE_EXACT);
        final ArrayList<String> keywordList = (ArrayList<String>) requestParams.getObject(KEYWORD_LIST);
        final String source = requestParams.getString(SOURCE, "");

        String shopId = shopInfoRepository.getShopId();
        List<AddKeywordDomainModelDatum> addKeywordDomainModelDatumList = new ArrayList<>();
        int size = keywordList.size();
        for (int i = 0; i < size; i++) {
            AddKeywordDomainModelDatum addKeywordDomainModelDatum = new AddKeywordDomainModelDatum(
                    keywordList.get(i),
                    keywordType,
                    groupId,
                    shopId,
                    source
            );
            addKeywordDomainModelDatumList.add(addKeywordDomainModelDatum);
        }
        AddKeywordDomainModel addKeywordDomainModel = new AddKeywordDomainModel(addKeywordDomainModelDatumList);
        return topAdsKeywordRepository.addKeyword(addKeywordDomainModel);
    }

    public static RequestParams createRequestParam(String groupId,
                                                   @KeywordTypeDef int keywordType,
                                                   ArrayList<String> keywordList,
                                                   String source) {
        RequestParams params = RequestParams.create();
        params.putString(GROUP_ID, groupId);
        params.putInt(KEYWORD_TYPE, keywordType);
        params.putObject(KEYWORD_LIST, keywordList);
        params.putString(SOURCE, source);
        return params;
    }
}
