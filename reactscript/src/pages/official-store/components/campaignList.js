import React from 'react';
import PropTypes from 'prop-types';
import {
  Text,
  View,
  StyleSheet,
  FlatList,
  Image,
  ScrollView,
  TouchableOpacity,
  TouchableWithoutFeedback,
  Dimensions
} from 'react-native';
import unescape from 'lodash/unescape'
import { NavigationModule, NetworkModule } from 'NativeModules';
import WishListButton from '../common/Wishlist/WishlistButton';

const { width, height } = Dimensions.get('window')

const CampaignList = ({ campaigns, User_ID }) => {
  return (
    <View style={styles.container}>
        <FlatList
          data={campaigns}
          keyExtractor={item => item.banner_id}
          renderItem={this.renderCampaign}
          initialNumToRender={1}
        />
    </View>
  );
};

this.renderCampaign = (c) => {
  const {
    productCell,
    productImageWrapper,
    productImage,
    productName,
    productGridNormalPrice,
    productGridNormalPriceText,
    priceWrapper,
    price,
    productGridCampaignRate,
    productGridCampaignRateText,
    priceContainer,
    badgeImageContainer,
    badgeImage,
    productBadgeWrapper,
    productLabel,
    labelText,
    productCashback,
    cashbackText,
    shopSection,
    shopImageWrapper,
    shopImage,
    shopNameWrapper,
    titleText,
    viewAll,
    viewAllText
   } = styles;

  const item = c.item;
  const products = c.item.Products || [];
  const productGrid = [];

  if (products.length > 0) {
    for (let i = 0; i < products.length; i += 2) {
      const productRow = [];
      for (let j = i; j < i + 2; j += 1) {
        // const dataProducts = products[j].data;
        if (!products[j]) {
          break;
        }

        productRow.push(
          <View style={productCell} key={products[j].data.id}>
            <TouchableWithoutFeedback onPress={() => {
                NavigationModule.navigate(products[j].data.url_app, "")
              }}>
              <View>
                <View style={productImageWrapper}>
                  <Image source={{ uri: products[j].data.image_url }} style={productImage} />
                </View>
                <Text
                  style={productName}
                  ellipsizeMode='tail'
                  numberOfLines={2}>{unescape(products[j].data.name)}</Text>
              </View>
            </TouchableWithoutFeedback>
            <View style={priceContainer}>
              <View style={productGridNormalPrice}>
                {
                  products[j].data.discount_percentage && (
                    <View style={{ height: 15 }}>
                      <Text style={productGridNormalPriceText}>{products[j].data.original_price}</Text>
                    </View>
                  )
                }
              </View>
              <View style={priceWrapper}>
                <Text style={price}>{products[j].data.price}</Text>
                { products[j].data.discount_percentage && (
                  <View style={productGridCampaignRate}>
                    <Text style={productGridCampaignRateText}>{`${products[j].data.discount_percentage}% OFF`}</Text>
                  </View>)
                }
              </View>
            </View>
            <View style={productBadgeWrapper}>
              {
                products[j].data.labels.map((l) => {
                  let labelTitle = l.title;
                  if (l.title.indexOf('Cashback') > -1) {
                    labelTitle = 'Cashback';
                  }
                  switch (labelTitle) {
                    case 'PO':
                    case 'Grosir':
                      return (
                        <View style={productLabel} key={products[j].data.id}>
                          <Text style={labelText}>{l.title}</Text>
                        </View>);
                    case 'Cashback':
                      return (
                        <View style={productCashback} key={products[j].data.id}>
                          <Text style={cashbackText}>{l.title}</Text>
                        </View>
                      );
                    default:
                      return null;
                  }
                })
              }
            </View>
            <TouchableWithoutFeedback onPress={() => {
              NavigationModule.navigate(products[j].data.shop.url_app, "")}}>
              <View style={shopSection}>
                <View style={shopImageWrapper}>
                  <Image source={{ uri: products[j].brand_logo }} style={shopImage} />
                </View>
                <View style={shopNameWrapper}>
                  <Text
                    style={{ lineHeight: 15 }}
                    ellipsizeMode='tail'
                    numberOfLines={1}>{unescape(products[j].data.shop.name)}</Text>
                </View>
                { products[j].data.badges.map(b => (b.title === 'Free Return' ? (
                  <View key={products[j].data.id} style={badgeImageContainer}>
                    <Image source={{ uri: b.image_url }} style={badgeImage} />
                  </View>) : null))
                }
              </View>
            </TouchableWithoutFeedback>
            <WishListButton
              isWishlist={products[j].data.is_wishlist || false}
              productId={products[j].data.id} />
          </View>
        );
      }
      productGrid.push(
        <View style={styles.productRow} key={i}>
          {productRow}
        </View>
      );
    }
  }

  return (
    <View style={{ marginBottom: 10, backgroundColor: '#FFF', borderTopWidth: 1, borderColor: '#e0e0e0' }}>
      {
        item.html_id === 6 ? null : <Text style={titleText}>{item.title}</Text>
      }
      {
        item.html_id === 6 ? (
          <TouchableWithoutFeedback onPress={() => NavigationModule.navigateWithMobileUrl(item.redirect_url_app, item.redirect_url_mobile, "")}>
            <Image source={{ uri: item.image_url }} style={styles.campaignImage} resizeMode={'contain'} />
          </TouchableWithoutFeedback>
        ) :
          (
            <TouchableWithoutFeedback onPress={() => {
              NavigationModule.navigateWithMobileUrl(item.redirect_url_app, item.redirect_url_mobile, "")}}>
              <Image source={{ uri: item.mobile_url }} style={styles.campaignImage} resizeMode={'contain'} />
            </TouchableWithoutFeedback>
          )
      }
      {productGrid}
      {
        item.html_id === 6 ? null : (<View style={viewAll}>
          <TouchableOpacity onPress={() => NavigationModule.navigate(item.redirect_url_app, '')}>
            <Text style={viewAllText}>Lihat Semua &gt;</Text>
          </TouchableOpacity>
        </View>)
      }
    </View >
  );
};

this.onClick = () => {

};

CampaignList.propTypes = {
  campaigns: PropTypes.arrayOf(PropTypes.object).isRequired
};

const styles = StyleSheet.create({
  priceContainer: {
    height: 40,
  },
  titleText: {
    fontSize: 16,
    fontWeight: '600',
    margin: 10
  },
  productRow: {
    flex: 1,
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderColor: '#e0e0e0'
  },
  productCell: {
    flex: 1 / 2,
    borderRightWidth: 1,
    borderColor: '#e0e0e0'
  },
  priceWrapper: {
    height: 20,
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 3,
  },
  price: {
    color: '#ff5722',
    fontSize: 13,
    fontWeight: '600',
    lineHeight: 15,
    paddingHorizontal: 10
  },
  productName: {
    fontSize: 13,
    fontWeight: '600',
    color: 'rgba(0,0,0,.7)',
    height: 40,
    paddingHorizontal: 10,
  },
  productImageWrapper: {
    borderBottomWidth: 1,
    borderColor: 'rgba(255,255,255,0)',
    padding: 10
  },
  productImage: {
    height: 185,
    borderRadius: 3
  },
  productBadgeWrapper: {
    height: 27,
    paddingVertical: 5,
    paddingHorizontal: 10,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center'
  },
  productCashback: {
    borderRadius: 3,
    marginRight: 3,
    padding: 3,
    backgroundColor: '#42b549'
  },
  cashbackText: {
    color: '#fff',
    fontSize: 10
  },
  shopSection: {
    flex: 1,
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderColor: '#e0e0e0',
    justifyContent: 'center'
  },
  shopImage: {
    width: 28,
    height: 28
  },
  shopImageWrapper: {
    width: 30,
    height: 30,
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  shopNameWrapper: {
    flex: 3 / 4,
    marginTop: 7,
    marginLeft: 10,
    marginBottom: 5,
    marginRight: 0
  },
  viewAll: {
    paddingVertical: 15,
    paddingHorizontal: 10,
    borderBottomWidth: 1,
    borderColor: '#e0e0e0',
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'flex-end'
  },
  viewAllText: {
    color: '#42b549',
    fontSize: 13
  },
  productLabel: {
    padding: 3,
    borderColor: '#e0e0e0',
    borderWidth: 1,
    marginRight: 3,
    backgroundColor: '#fff',
    borderRadius: 3
  },
  labelText: {
    fontSize: 10
  },
  productGridPrice: {
    height: 34
  },
  productGridNormalPrice: {
    paddingHorizontal: 10
  },
  productGridNormalPriceText: {
    fontSize: 10,
    fontWeight: '600',
    textDecorationLine: 'line-through'
  },
  badgeImageContainer: {
    width: 20,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  badgeImage: {
    height: 16,
    width: 16
  },
  productGridCampaignRate: {
    backgroundColor: '#f02222',
    padding: 3,
    borderRadius: 3,
    marginLeft: 5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  productGridCampaignRateText: {
    color: '#fff',
    fontSize: 10,
    textAlign: 'center'
  },
  campaignImage: {
    width,
    height: width * 0.27,
  },
});

export default CampaignList;
