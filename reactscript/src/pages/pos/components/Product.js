import React, { PureComponent } from 'react'
import {
  View,
  Text,
  StyleSheet,
  Linking,
  TouchableWithoutFeedback,
  Image,
  Dimensions,
} from 'react-native'
import PropTypes from 'prop-types'

class Product extends PureComponent {
  render() {
    const product = this.props.product
    return (
      <View style={styles.container}>
        <TouchableWithoutFeedback onPress={() => Linking.openURL(`tokopedia://product/${product.id}`)}>
          <View>
            <View style={styles.productImageWrapper}>
              <Image source={{ uri: product.image_url }} style={styles.productImage} />
            </View>
            <View
              style={{
                borderBottomColor: '#e0e0e0',
                borderBottomWidth: 1,
              }}
            />
            <Text
              style={styles.productName}
              ellipsizeMode='tail'
              numberOfLines={3}>{product.name}</Text>
          </View>
        </TouchableWithoutFeedback>
        <View style={styles.priceWrapper}>
          <Text style={styles.price}>{product.price}</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    borderWidth: 1,
    borderColor: '#e0e0e0',
    width: '33%',
    backgroundColor: 'white',
    borderRadius: 3,
    margin: 3,
  },
  productImageWrapper: {
    alignItems: 'center',
    borderBottomWidth: 1,
    borderColor: 'rgba(255,255,255,0)',
    paddingVertical: 10,
  },
  productImage: {
    borderRadius: 3,
    width: 100,
    height: 100,
    resizeMode: 'cover'
  },
  productName: {
    fontSize: 13,
    fontWeight: '600',
    color: 'rgba(0,0,0,.7)',
    height: 33.8,
    paddingHorizontal: 10,
    paddingTop: 3,
  },
  priceWrapper: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    height: 34
  },
  price: {
    color: '#ff5722',
    fontSize: 13,
    fontWeight: '600',
    lineHeight: 20,
    paddingHorizontal: 10
  },
})

export default Product