import React from 'react'
import { View, Image, StyleSheet, Text, Platform, TouchableHighlight, Dimensions } from 'react-native'
import PropTypes from 'prop-types'
import unescape from 'lodash/unescape'
import { NavigationModule } from 'NativeModules'

const { width, height } = Dimensions.get('window')

const styles = StyleSheet.create({
  productCell: {
    width: '50%',
    borderColor: '#e0e0e0',
    backgroundColor: '#FFF',
  },
  productImageWrapper: {
    padding: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  productImage: {
    width: (width - 40) / 2,
    height: (width - 40) / 2,
    ...Platform.select({
      ios: {
        resizeMode: 'contain'
      },
      android: {
        borderWidth: 1,
        borderRadius: 3,
        borderColor: 'transparent',
        overlayColor: '#FFF',
      }
    })
  },
  productName: {
    fontSize: 13,
    fontWeight: '600',
    color: 'rgba(0,0,0,.7)',
    height: 41,
    paddingHorizontal: 10,
  },
  priceContainer: {
    height: 40,
  },
  productGridNormalPrice: {
    paddingHorizontal: 10,
  },
  productGridNormalPriceText: {
    fontSize: 10,
    fontWeight: '600',
    textDecorationLine: 'line-through',
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
    paddingHorizontal: 10,
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
    textAlign: 'center',
  },
  productBadgeWrapper: {
    height: 27,
    paddingVertical: 5,
    paddingHorizontal: 10,
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  productCashback: {
    borderRadius: 3,
    marginRight: 3,
    padding: 3,
    backgroundColor: '#42b549',
  },
  cashbackText: {
    color: '#fff',
    fontSize: 10,
  },
  labelText: {
    fontSize: 10,
  },
  shopSection: {
    flexDirection: 'row',
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderColor: '#e0e0e0',
    justifyContent: 'center',
    backgroundColor: 'transparent',
    borderTopWidth: 1,
  },
  shopImage: {
    width: 28,
    height: 28,
    ...Platform.select({
      ios: {
        resizeMode: 'contain'
      },
      android: {
        borderWidth: 1,
        borderRadius: 3,
        borderColor: '#E0E0E0',
        overlayColor: '#FFF',
      }
    })
  },
  shopNameWrapper: {
    flex: 3 / 4,
    marginTop: 7,
    marginLeft: 10,
    marginBottom: 5,
    marginRight: 0,
  },
  badgeImageContainer: {
    width: 20,
    height: 28,
    justifyContent: 'center',
    alignItems: 'center',
  },
  badgeImage: {
    height: 20,
    width: 20,
  },
  productLabel: {
    padding: 3,
    borderColor: '#e0e0e0',
    borderWidth: 1,
    marginRight: 3,
    backgroundColor: '#fff',
    borderRadius: 3,
  },
})

const Product = ({ product, index }) => (
  <View style={[styles.productCell, index % 2 === 0 ? {borderRightWidth: 1} : {borderRightWidth: 0}]}>
    <TouchableHighlight underlayColor={'#FFF'} onPress={() => {
      NavigationModule.navigateWithMobileUrl(product.data.url_app, product.data.url, '')}}>
      <View>
        <View style={styles.productImageWrapper}>
          <Image source={{ uri: product.data.image_url }} style={styles.productImage} />
        </View>
        <Text
          style={styles.productName}
          ellipsizeMode="tail"
          numberOfLines={2}>{unescape(product.data.name)}
        </Text>
        <View style={styles.priceContainer}>
          <View style={styles.productGridNormalPrice}>
            {
              product.data.discount_percentage && (
                <View style={{ height: 15 }}>
                  <Text style={styles.productGridNormalPriceText}>{product.data.original_price}</Text>
                </View>
              )
            }
          </View>
          <View style={styles.priceWrapper}>
            <Text style={styles.price}>{product.data.price}</Text>
            {product.data.discount_percentage && (
              <View style={styles.productGridCampaignRate}>
                <Text style={styles.productGridCampaignRateText}>{`${product.data.discount_percentage}% OFF`}</Text>
              </View>)
            }
          </View>
        </View>
      </View>
    </TouchableHighlight>
    <View style={styles.productBadgeWrapper}>
      {
        product.data.labels.map((l, i) => {
          let labelTitle = l.title;
          if (l.title.indexOf('Cashback') > -1) {
            labelTitle = 'Cashback';
          }
          switch (labelTitle) {
            case 'PO':
            case 'Grosir':
              return (
                <View style={styles.productLabel} key={i}>
                  <Text style={styles.labelText}>{l.title}</Text>
                </View>);
            case 'Cashback':
              return (
                <View style={styles.productCashback} key={i}>
                  <Text style={styles.cashbackText}>{l.title}</Text>
                </View>
              );
            default:
              return null;
          }
        })
      }
    </View>

    {/* Brand Section */}
    <TouchableHighlight underlayColor={'#FFF'} onPress={() => {
      console.log(product)
      NavigationModule.navigateWithMobileUrl(product.data.shop.url_app, product.data.shop.url, '')}}>
      <View style={styles.shopSection}>
        <Image source={{ uri: product.brand_logo }} style={styles.shopImage} />
        <View style={styles.shopNameWrapper}>
          <Text
            style={{ lineHeight: 15 }}
            ellipsizeMode="tail"
            numberOfLines={1}>{unescape(product.data.shop.name)}</Text>
        </View>
        {
          product.data.badges.map(b => (b.title === 'Free Return' ? (
          <View key={product.data.id} style={styles.badgeImageContainer}>
            <Image source={{ uri: b.image_url }} style={styles.badgeImage} />
          </View>) : null))
        }
      </View>
    </TouchableHighlight>
  </View>
)

export default Product
