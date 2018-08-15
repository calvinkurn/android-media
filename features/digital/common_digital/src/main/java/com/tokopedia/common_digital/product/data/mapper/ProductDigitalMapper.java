package com.tokopedia.common_digital.product.data.mapper;

import com.tokopedia.common_digital.common.MapperDataException;
import com.tokopedia.common_digital.product.presentation.model.AdditionalFeature;
import com.tokopedia.common_digital.product.presentation.model.BannerData;
import com.tokopedia.common_digital.product.presentation.model.CategoryData;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.GuideData;
import com.tokopedia.common_digital.product.presentation.model.HistoryClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.OrderClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.ProductDigitalData;
import com.tokopedia.common_digital.product.presentation.model.Promo;
import com.tokopedia.common_digital.product.presentation.model.Rule;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.common_digital.product.data.response.Field;
import com.tokopedia.common_digital.product.data.response.GuideEntity;
import com.tokopedia.common_digital.product.data.response.OperatorBannerEntity;
import com.tokopedia.common_digital.product.data.response.RechargeCategoryDetail;
import com.tokopedia.common_digital.product.data.response.RechargeFavoritNumber;
import com.tokopedia.common_digital.product.data.response.RechargeFavoritNumberResponseEntity;
import com.tokopedia.common_digital.product.data.response.RechargeResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalMapper {

    public ProductDigitalData transformCategoryData(RechargeResponseEntity entity) throws MapperDataException {
        CategoryData categoryData = new CategoryData();
        if (entity != null && entity.getRechargeCategoryDetail() != null) {
            RechargeCategoryDetail categoryDetail = entity.getRechargeCategoryDetail();

            categoryData.setTitleText(categoryDetail.getTitle());
            categoryData.setCategoryId(categoryDetail.getId());
            categoryData.setDefaultOperatorId(categoryDetail.getDefaultOperatorId());
            categoryData.setIcon(categoryDetail.getIcon());
            categoryData.setIconUrl(categoryDetail.getIconUrl());
            categoryData.setName(categoryDetail.getName());
            categoryData.setInstantCheckout(categoryDetail.getInstantCheckout());
            categoryData.setNew(categoryDetail.getIsNew());
            categoryData.setSlug(categoryDetail.getSlug());
            categoryData.setOperatorStyle(categoryDetail.getOperatorStyle());
            categoryData.setOperatorLabel(categoryDetail.getOperatorLabel());
            if (categoryDetail.getAdditionalFeature() != null) {
                categoryData.setAdditionalFeature(transformAdditionalFeature(categoryDetail.getAdditionalFeature()));
            }

            categoryData.setClientNumberList(transformClientNumberList(categoryDetail));
            categoryData.setOperatorList(transformOperators(categoryDetail));
            categoryData.setBannerDataListIncluded(transformBanners(categoryDetail.getBanners()));
            categoryData.setOtherBannerDataListIncluded(transformBanners(categoryDetail.getOtherBanners()));
            categoryData.setGuideDataList(transformGuides(categoryDetail.getGuides()));
        }

        return new ProductDigitalData.Builder()
                .historyClientNumber(new HistoryClientNumber.Builder()
                        .lastOrderClientNumber(getLastOrder(entity.getRechargeFavoritNumberResponseEntity()))
                        .recentClientNumberList(getOrderList(entity.getRechargeFavoritNumberResponseEntity()))
                        .build())
                .categoryData(categoryData)
                .bannerDataList(categoryData.getBannerDataListIncluded())
                .otherBannerDataList(categoryData.getOtherBannerDataListIncluded())
                .guideDataList(categoryData.getGuideDataList())
                .build();
    }

    private AdditionalFeature transformAdditionalFeature(
            com.tokopedia.common_digital.product.data.response.AdditionalFeature additionalFeature) {
        return new AdditionalFeature(additionalFeature.getText(), additionalFeature.getButtonText(),
                additionalFeature.getId());
    }

    /**
     * Helper function to transfor client number list
     *
     * @param entity
     * @return
     */
    private List<ClientNumber> transformClientNumberList(RechargeCategoryDetail entity) {
        List<ClientNumber> clientNumberCategoryList = new ArrayList<>();

        if (entity != null && entity.getClientNumber() != null && entity.getClientNumber().getName().equalsIgnoreCase(ClientNumber.DEFAULT_TYPE_CONTRACT)) {
            ClientNumber clientNumberCategory = new ClientNumber();
            clientNumberCategory.setName(entity.getClientNumber().getName());
            clientNumberCategory.set_default(entity.getClientNumber().getDefault());
            clientNumberCategory.setType(entity.getClientNumber().getType());
            clientNumberCategory.setPlaceholder(entity.getClientNumber().getPlaceholder());
            clientNumberCategory.setText(entity.getClientNumber().getText());
            List<Validation> validationCategoryList
                    = new ArrayList<>();
            for (com.tokopedia.common_digital.product.data.response.Validation validation
                    : entity.getClientNumber().getValidation()) {
                Validation validationCategory =
                        new Validation();
                validationCategory.setError(validation.getError());
                validationCategory.setRegex(validation.getRegex());
                validationCategoryList.add(validationCategory);
            }
            clientNumberCategory.setValidation(validationCategoryList);
            clientNumberCategoryList.add(clientNumberCategory);
        }

        return clientNumberCategoryList;
    }

    /**
     * Helper function to transform operator list
     *
     * @param entity
     * @return
     */
    private List<Operator> transformOperators(RechargeCategoryDetail entity) {
        List<Operator> operatorCategoryList = new ArrayList<>();

        if (entity == null) return operatorCategoryList;

        for (OperatorBannerEntity categoryDetailIncluded : entity.getOperators()) {
            Operator operatorCategory = new Operator();
            operatorCategory.setName(categoryDetailIncluded.getAttributes().getName());
            operatorCategory.setDefaultProductId(
                    categoryDetailIncluded.getAttributes().getDefaultProductId()
            );
            operatorCategory.setImage(categoryDetailIncluded.getAttributes().getImage());
            operatorCategory.setUssdCode(categoryDetailIncluded.getAttributes().getUssd());
            operatorCategory.setLastorderUrl(
                    categoryDetailIncluded.getAttributes().getLastorderUrl()
            );
            operatorCategory.setOperatorId(categoryDetailIncluded.getId());
            operatorCategory.setPrefixList(categoryDetailIncluded.getAttributes().getPrefix());
            operatorCategory.setOperatorType(categoryDetailIncluded.getType());
            List<Product> productOperatorList = new ArrayList<>();
            for (com.tokopedia.common_digital.product.data.response.Product product
                    : categoryDetailIncluded.getAttributes().getProduct()) {
                if (product.getAttributes().getStatus() != Product.STATUS_INACTIVE) {
                    Product productOperator = new Product();
                    productOperator.setDesc(product.getAttributes().getDesc());
                    productOperator.setDetail(product.getAttributes().getDetail());
                    productOperator.setDetailCompact(product.getAttributes().getDetailCompact());
                    productOperator.setDetailUrl(product.getAttributes().getDetailUrl());
                    productOperator.setDetailUrlText(product.getAttributes().getDetailUrlText());
                    productOperator.setInfo(product.getAttributes().getInfo());
                    productOperator.setPrice(product.getAttributes().getPrice());
                    productOperator.setPricePlain(product.getAttributes().getPricePlain());
                    productOperator.setProductType(product.getType());
                    productOperator.setProductId(product.getId());
                    productOperator.setStatus(product.getAttributes().getStatus());
                    if (product.getAttributes().getPromo() != null) {
                        Promo productPromo = new Promo();
                        productPromo.setBonusText(product.getAttributes().getPromo().getBonusText());
                        productPromo.setId(product.getAttributes().getPromo().getId());
                        productPromo.setNewPrice(product.getAttributes().getPromo().getNewPrice());
                        productPromo.setNewPricePlain(
                                product.getAttributes().getPromo().getNewPricePlain()
                        );
                        productPromo.setTag(product.getAttributes().getPromo().getTag());
                        productPromo.setTerms(product.getAttributes().getPromo().getTerms());
                        productPromo.setValueText(product.getAttributes().getPromo().getValueText());
                        productOperator.setPromo(productPromo);
                    }
                    productOperatorList.add(productOperator);
                }
            }
            List<ClientNumber> clientNumberOperatorList = new ArrayList<>();
            for (Field field
                    : categoryDetailIncluded.getAttributes().getFields()) {
                ClientNumber clientNumberOperator = new ClientNumber();
                clientNumberOperator.setName(field.getName());
                clientNumberOperator.set_default(field.getDefault());
                clientNumberOperator.setType(field.getType());
                clientNumberOperator.setPlaceholder(field.getPlaceholder());
                clientNumberOperator.setText(field.getText());
                List<Validation> validationCategoryList
                        = new ArrayList<>();
                for (com.tokopedia.common_digital.product.data.response.Validation validation
                        : field.getValidation()) {
                    Validation validationCategory =
                            new Validation();
                    validationCategory.setError(validation.getError());
                    validationCategory.setRegex(validation.getRegex());
                    validationCategoryList.add(validationCategory);
                }
                clientNumberOperator.setValidation(validationCategoryList);
                clientNumberOperatorList.add(clientNumberOperator);
            }
            operatorCategory.setProductList(productOperatorList);
            operatorCategory.setClientNumberList(clientNumberOperatorList);

            Rule operatorRule = new Rule();
            operatorRule.setMaximumLength(
                    categoryDetailIncluded.getAttributes().getRule().getMaximumLength()
            );
            operatorRule.setEnableVoucher(
                    categoryDetailIncluded.getAttributes().getRule().getEnableVoucher()
            );
            operatorRule.setShowPrice(
                    categoryDetailIncluded.getAttributes().getRule().getShowPrice()
            );
            operatorRule.setProductText(
                    categoryDetailIncluded.getAttributes().getRule().getProductText()
            );
            operatorRule.setProductViewStyle(
                    categoryDetailIncluded.getAttributes().getRule().getProductViewStyle()
            );
            operatorRule.setButtonText(
                    categoryDetailIncluded.getAttributes().getRule().getButtonText()
            );
            operatorCategory.setRule(operatorRule);

            operatorCategoryList.add(operatorCategory);
        }

        return operatorCategoryList;
    }

    /**
     * Helper function to transform banner list
     *
     * @param entity
     * @return
     */
    private List<BannerData> transformBanners(List<OperatorBannerEntity> entity) {
        List<BannerData> bannerDataList = new ArrayList<>();

        if (entity != null) {
            for (OperatorBannerEntity bannerEntity : entity) {
                bannerDataList.add(
                        new BannerData.Builder()
                                .id(bannerEntity.getId())
                                .type(bannerEntity.getType())
                                .title(bannerEntity.getAttributes().getTitle())
                                .subtitle(bannerEntity.getAttributes().getSubTitle())
                                .promocode(bannerEntity.getAttributes().getPromocode())
                                .image(bannerEntity.getAttributes().getImage())
                                .dataTitle(bannerEntity.getAttributes().getDataTitle())
                                .link(bannerEntity.getAttributes().getLink())
                                .build()
                );
            }
        }

        return bannerDataList;
    }


    private OrderClientNumber getLastOrder(RechargeFavoritNumberResponseEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderClientNumber orderClientNumber = new OrderClientNumber();

        orderClientNumber.setCategoryId(entity.getCategoryId());
        orderClientNumber.setClientNumber(entity.getClientNumber());
        orderClientNumber.setOperatorId(entity.getOperatorId());
        orderClientNumber.setProductId(entity.getProductId());

        return orderClientNumber;
    }


    private List<OrderClientNumber> getOrderList(RechargeFavoritNumberResponseEntity entity) {
        List<OrderClientNumber> clientNumbers = new ArrayList<>();

        if (entity != null && entity.getList() != null) {

            for (RechargeFavoritNumber favoritNumber : entity.getList()) {
                OrderClientNumber orderClientNumber = new OrderClientNumber();

                orderClientNumber.setCategoryId(favoritNumber.getCategoryId());
                orderClientNumber.setClientNumber(favoritNumber.getClientNumber());
                orderClientNumber.setOperatorId(favoritNumber.getOperatorId());
                orderClientNumber.setProductId(favoritNumber.getProductId());
                orderClientNumber.setName(favoritNumber.getLabel());

                clientNumbers.add(orderClientNumber);
            }
        }

        return clientNumbers;
    }

    /**
     * Helper function to transform guides list
     *
     * @param entityList
     * @return
     */
    private List<GuideData> transformGuides(List<GuideEntity> entityList) {
        List<GuideData> guideDataList = new ArrayList<>();

        if (entityList != null) {
            for (GuideEntity item : entityList) {
                guideDataList.add(
                        new GuideData.Builder()
                                .id(item.getId())
                                .type(item.getType())
                                .title(item.getAttribute().getTitle())
                                .sourceLink(item.getAttribute().getSourceLink())
                                .build()
                );
            }
        }

        return guideDataList;
    }
}
