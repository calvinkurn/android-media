import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet,
  TouchableWithoutFeedback,
  Image,
  Dimensions,
} from 'react-native'
import PropTypes from 'prop-types'
import { NavigationModule } from 'NativeModules'

class Product extends PureComponent {
  render() {
    const product = this.props.product
    return (
      <View style={styles.container}>
        <TouchableWithoutFeedback onPress={() => NavigationModule.navigate(`posapp://product/${product.product_id}`, '')}>
          <View>
            <View style={styles.productImageWrapper}>
              <Image source={{ uri: product.product_image_300 }} style={styles.productImage} />
            </View>
            <View style={{ borderBottomColor: '#e0e0e0', borderBottomWidth: 1 }} />
            <View style={{ height: 50 }}>
              <Text style={styles.productName} ellipsizeMode='tail' numberOfLines={3}>{product.product_name}</Text>
            </View>
          </View>
        </TouchableWithoutFeedback>
        <View style={styles.priceWrapper}>
          <Text style={styles.price}>{product.product_price}</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    borderWidth: 1,
    borderColor: '#e0e0e0',
    width: '32%',
    backgroundColor: 'white',
    borderRadius: 3,
    marginRight: '2%',
    marginBottom: 16,
  },
  productImageWrapper: {
    alignItems: 'center',
    borderBottomWidth: 1,
    borderColor: 'rgba(255,255,255,0)',
    paddingVertical: 10,
  },
  productImage: {
    borderRadius: 3,
    width: 230,
    height: 230,
    resizeMode: 'cover'
  },
  productName: {
    fontSize: 13,
    fontWeight: '200',
    color: 'rgba(0,0,0,.7)',
    paddingHorizontal: 16,
    paddingTop: 8,
  },
  priceWrapper: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center'
  },
  price: {
    color: '#ff5722',
    fontSize: 14,
    fontWeight: '600',
    lineHeight: 20,
    paddingHorizontal: 16,
    marginBottom: 24
  },
})

export default Product