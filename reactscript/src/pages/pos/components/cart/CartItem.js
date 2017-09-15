import React, { Component } from 'react'
import { View, StyleSheet, Image, TouchableWithoutFeedback } from 'react-native'
import { Text } from '../../common/TKPText'
import PopUp from '../../common/TKPPopupModal'
import { config } from '../../lib/config'

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
      <View style={styles.container}>
        <PopUp
          visible={this.state.show}
          animationType='fade'
          onBackPress={() => { this.togglePopUp(false) }}
          title='Konfirmasi Pembatalan'
          subTitle={`${item.name} - senilai ${item.price}`}
          onSecondOptionTap={() => { this.removeItem(item.id) }}
          onFirstOptionTap={() => { this.togglePopUp(false) }}
          firstOptionText='Tidak'
          secondOptionText='Ya'
          onCloseIconTap={() => { this.togglePopUp(false) }}
        />
        <View style={{ width: '8%' }}>
          <TouchableWithoutFeedback
            onPress={() => { this.togglePopUp(true) }} >
            <Image source={{uri: config.imageBasePath + 'trash.png'}} style={styles.trashImage} />
          </TouchableWithoutFeedback>
        </View>
        <View style={{ width: '20%' }}>
          <Image source={{ uri: item.imageUrl }} style={styles.imageStyle} />
        </View>
        <View style={{ width: '40%' }}>
          <Text ellipsizeMode='tail' numberOfLines={2} style={styles.productName}>{item.name}</Text>
          <Text style={styles.price}>Rp. {item.price}</Text>
        </View>
        <View style={styles.qtyContainer}>
          <Text style={styles.qtyLabel}>Qty</Text>
          <View style={styles.qtyControlContainer}>
            <TouchableWithoutFeedback
              disabled={item.qty === 1}
              onPress={() => { onDecr(item.id) }}>
              <View>
                <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/btn_minus.png' }} style={styles.qtyControlImage}></Image>
              </View>
            </TouchableWithoutFeedback>
            <View style={styles.qty}>
              <Text style={styles.qtyText}> {item.qty} </Text>
            </View>
            <TouchableWithoutFeedback
              onPress={() => { onIncr(item.id) }}>
              <View>
              <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/btn_plus.png' }} style={styles.qtyControlImage}></Image>
              </View>
            </TouchableWithoutFeedback>
          </View>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    backgroundColor: '#fff',
    borderBottomColor: '#e0e0e0',
    borderBottomWidth: 1,
    paddingHorizontal: 5,
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
    fontSize: 18,
    lineHeight: 20,
    paddingVertical: 10,
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
    width: '22%',
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
    color: '#e0e0e0',
    fontWeight: 'bold',
  },
  qtyText: {
    fontSize: 16,
    paddingHorizontal: 10,
    paddingVertical: 3,
  },
  trashImage: {
    width: 30,
    height: 30,
    resizeMode: 'contain',
    left: 10,
  },
  productName: {
    fontSize: 18
  }
})

export default CartItem
