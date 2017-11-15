import React, { Component } from 'react'
import { View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native'
import { Text } from '../../../common/TKPText'
import PopUp from '../../../common/TKPPopupModal'
// import { config } from '../../../lib/config'
import { icons } from '../../../lib/config'

class CartItem extends Component {
  constructor(props) {
    super(props)
    this.state = { show: false }
  }

  togglePopUp = (visible) => {
    this.setState({ show: visible })
  }

  removeItem = (id) => {
    this.togglePopUp(false)
    this.props.onRemove(id)
  }

  render() {
    const item = this.props.item
    const onIncr = this.props.onIncr
    const onDecr = this.props.onDecr
    const onRemove = this.props.onRemove

    return (
      <View>
        <View style={styles.container}>
          <PopUp
            visible={this.state.show}
            animationType='none'
            onBackPress={() => { this.togglePopUp(false) }}
            title='Konfirmasi Pembatalan'
            subTitle={`${item.product.product_name} - senilai ${item.product.product_price}`}
            onSecondOptionTap={() => { this.removeItem(item.product_id) }}
            onFirstOptionTap={() => { this.togglePopUp(false) }}
            firstOptionText='Tidak'
            secondOptionText='Ya'
            onCloseIconTap={() => { this.togglePopUp(false) }}
          />
          <View style={{ width: '8%' }}>
            <TouchableWithoutFeedback
              onPress={() => { this.togglePopUp(true) }} >
              <Image source={{ uri: icons.trash }} style={styles.trashImage} />
            </TouchableWithoutFeedback>
          </View>
          <View style={{ width: '10%' }}>
            <Image source={{ uri: item.product.product_image_300 }} style={styles.imageStyle} />
          </View>
          <View style={{ width: '50%', alignSelf: 'flex-start' }}>
            <Text ellipsizeMode='tail' numberOfLines={2} style={styles.productName}>{item.product.product_name}</Text>
            <Text style={styles.price}>{item.product.product_price}</Text>
          </View>
          <View style={styles.qtyContainer}>
            <Text style={styles.qtyLabel}>Qty</Text>
            <View style={styles.qtyControlContainer}>
              <TouchableWithoutFeedback
                disabled={item.qty === 1}
                onPress={() => { onDecr(item.id, item.product_id, item.quantity) }}>
                <View>
                  <Image source={{ uri: icons.btn_minus }} style={styles.qtyControlImage}></Image>
                </View>
              </TouchableWithoutFeedback>
              <View style={styles.qty}>
                <Text style={styles.qtyText}> {item.quantity} </Text>
              </View>
              <TouchableWithoutFeedback
                onPress={() => { onIncr(item.id, item.product_id, item.quantity) }}>
                <View>
                  <Image source={{ uri: icons.btn_plus }} style={styles.qtyControlImage}></Image>
                </View>
              </TouchableWithoutFeedback>
            </View>
          </View>
        </View>
        <View
          style={{
            borderBottomColor: '#e0e0e0',
            borderBottomWidth: 1,
            marginHorizontal: 32
          }}
        />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    paddingHorizontal: 32,
    paddingVertical: 30,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  imageStyle: {
    width: 80,
    height: 80,
  },
  price: {
    color: '#ff5722',
    fontSize: 16,
    lineHeight: 20,
    paddingVertical: 5,
  },
  qtyControlContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  qty: {
    paddingVertical: 4,
    paddingHorizontal: 8,
    borderColor: '#e0e0e0',
    borderTopWidth: 1,
    borderBottomWidth: 1,
  },
  qtyContainer: {
    width: '20%',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingRight: 10
  },
  trashImage: {
    width: 30,
    height: 30,
    resizeMode: 'contain',
    left: 10,
  },
  qtyControlImage: {
    width: 38,
    height: 38
  },
  qtyLabel: {
    fontSize: 16,
    fontWeight: '600',
  },
  qtyText: {
    fontSize: 14,
    paddingHorizontal: 10,
    paddingVertical: 4.5,
  },
  trashImage: {
    width: 30,
    height: 30,
    resizeMode: 'contain',
    left: 10,
  },
  productName: {
    fontSize: 16,
    fontWeight: '600',
    height: 48,
  }
})

export default CartItem