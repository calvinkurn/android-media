import React, { Component } from 'react'
import { Modal, View, Text, TouchableWithoutFeedback, Image, ScrollView } from 'react-native'
import CartItem from '../components/CartItem'
import Button from '../common/TKPPrimaryBtn'
import PopUp from '../common/TKPPopupModal'

export default class CartItemList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      showPopUp: false
    }
  }

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }

  remove = () => {
    this.setState({ showPopUp: false })
    this.props.onRemoveAllFromCart()
  }

  render() {
    const items = this.props.items
    const visible = this.props.visible
    const onBackPress = this.props.onBackPress
    const totalPrice = this.props.totalPrice
    const onIncrQty = this.props.onIncrQty
    const onDecrQty = this.props.onDecrQty
    const onRemoveFromCart = this.props.onRemoveFromCart
    const onRemoveAllFromCart = this.props.onRemoveAllFromCart

    return (
      <Modal
        animationType={'slide'}
        transparent={true}
        hardwareAccelerated={true}
        visible={this.props.visible}
        onRequestClose={onBackPress}>
        {items.length > 0 ?
          <View style={{ flex: 1 }}>
            <View style={{
              height: 55,
              backgroundColor: '#42b549',
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'space-around'
            }}>
              <View style={{ width: '10%', left: 10 }}>
                <TouchableWithoutFeedback onPress={onBackPress}>
                  <Image source={require('./img/icon_back.png')} />
                </TouchableWithoutFeedback>
              </View>
              <View style={{ width: '80%', left: 10 }}>
                <Text style={{ fontSize: 18, color: '#fff' }}>Keranjang Belanja</Text>
              </View>
              <View style={{ width: '10%' }}>
                <TouchableWithoutFeedback onPress={() => { this.toggleScreen(true) }}>
                  <Image source={require('./img/trash-all.png')} />
                </TouchableWithoutFeedback>
              </View>
            </View>
            <View style={styles.container}>
              <View>
                <PopUp
                  visible={this.state.showPopUp}
                  animationType='fade'
                  onBackPress={() => { this.toggleScreen(false) }}
                  title='Konfirmasi Pembatalan'
                  subTitle='Apakah Anda yakin untuk menghapus semua daftar belanjaan?'
                  onSecondOptionTap={this.remove}
                  onFirstOptionTap={() => { this.toggleScreen(false) }}
                  firstOptionText='Tidak'
                  secondOptionText='Ya'
                  onCloseIconTap={() => { this.toggleScreen(false) }}
                />
              </View>
              <ScrollView style={styles.itemListContainer}>
                {
                  items.map(i => <CartItem
                    item={i}
                    key={i.id}
                    onIncr={onIncrQty}
                    onDecr={onDecrQty}
                    onRemove={onRemoveFromCart}
                  />)
                }
              </ScrollView>
              <View style={styles.paymentContainer}>
                <Text>Total Pembayaran</Text>
                <Text>Rp {totalPrice}</Text>
              </View>
              <View style={{ marginTop: 20 }}>
                <Button
                  content='Checkout'
                  type='medium'
                />
              </View>
            </View></View> :
          <View style={styles.emptyContainer}>
            <View style={{
              height: 55,
              backgroundColor: '#42b549',
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'space-around'
            }}>
              <View style={{ width: '10%', left: 10 }}>
                <TouchableWithoutFeedback onPress={onBackPress}>
                  <Image source={require('./img/icon_back.png')} />
                </TouchableWithoutFeedback>
              </View>
              <View style={{ width: '80%', left: 10 }}>
                <Text style={{ fontSize: 18, color: '#fff' }}>Keranjang Belanja</Text>
              </View>
              <View style={{ width: '10%' }}>
              </View>
            </View>
            <View style={styles.emptyList}>
              <View style={{
                alignItems: 'center',
                justifyContent: 'center',
                height: '40%',
                backgroundColor: '#fff'
              }}>
                <Image source={require('./img/shopping-basket.png')} style={{ width: 100, height: 100 }} />
                <Text>Tidak Ada barang dalam keranjang</Text>
              </View>
              <View style={{ marginTop: 20 }}>
                <Button
                  content='Mulai Belanja'
                  type='medium'
                  onTap={onBackPress}
                />
              </View>
            </View>
          </View>
        }
      </Modal>
    )
  }
}

const styles = {
  paymentContainer: {
    flexDirection: 'row',
    marginTop: 10,
    backgroundColor: '#fff',
    justifyContent: 'space-between',
    paddingVertical: 10,
    paddingHorizontal: 5,
  },
  itemListContainer: {
    backgroundColor: '#fff',
  },
  emptyList: {
    paddingVertical: 10,
    paddingHorizontal: 15,
    backgroundColor: '#f1f1f1',
    flex: 1
  },
  emptyContainer: {
    flex: 1,
  },
  container: {
    flex: 1,
    backgroundColor: '#f1f1f1',
    paddingVertical: 10,
    paddingHorizontal: 15,
    backgroundColor: '#f1f1f1',
  },
  checkoutBtn: {
    height: 40,
    backgroundColor: '#ff5722',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#ff5722',
  },
  checkoutBtnText: {
    color: '#fff',
    fontSize: 13,
    fontWeight: '600'
  }
}